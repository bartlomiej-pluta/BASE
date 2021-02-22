package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.asset.view.select.SelectGraphicAssetFragment
import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.model.map.CreateLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.MoveLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.RemoveLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.RenameLayerCommand
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.model.layer.*
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.cell.TextFieldListCell
import javafx.util.StringConverter
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapLayersView : View() {
   private val undoRedoService: UndoRedoService by di()

   private val projectContext: ProjectContext by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()

   private val editorStateVM = find<EditorStateVM>()

   private var layersPane = ListView(mapVM.layers).apply {
      isEditable = true

      setCellFactory {
         LayerListViewItem { layer, name ->
            RenameLayerCommand(layer, name).let {
               it.execute()
               undoRedoService.push(it, scope)
            }
         }
      }

      editorStateVM.selectedLayerIndexProperty.bind(selectionModel.selectedIndexProperty())
      editorStateVM.selectedLayerProperty.bind(selectionModel.selectedItemProperty())
   }

   override val root = borderpane {
      center = layersPane

      bottom = toolbar {
         menubutton(graphic = FontIcon("fa-plus")) {
            item("Tile Layer", graphic = FontIcon("fa-th-large")) {
               action {
                  val layer = TileLayer("Layer ${mapVM.layers.size + 1}", mapVM.rows, mapVM.columns)
                  val command = CreateLayerCommand(mapVM.item, layer)
                  command.execute()
                  layersPane.selectionModel.select(mapVM.layers.size - 1)
                  undoRedoService.push(command, scope)
               }
            }

            item("Object Layer", graphic = FontIcon("fa-cube")) {
               action {
                  val layer = ObjectLayer("Layer ${mapVM.layers.size + 1}", mapVM.rows, mapVM.columns)
                  val command = CreateLayerCommand(mapVM.item, layer)
                  command.execute()
                  layersPane.selectionModel.select(mapVM.layers.size - 1)
                  undoRedoService.push(command, scope)
               }
            }

            item("Color Layer", graphic = FontIcon("fa-paint-brush")) {
               action {
                  val layer = ColorLayer("Layer ${mapVM.layers.size + 1}", 100, 100, 100, 100)
                  val command = CreateLayerCommand(mapVM.item, layer)
                  command.execute()
                  layersPane.selectionModel.select(mapVM.layers.size - 1)
                  undoRedoService.push(command, scope)
               }
            }

            item("Image Layer", graphic = FontIcon("fa-image")) {
               action {
                  val scope = UndoableScope()
                  find<SelectGraphicAssetFragment<ImageAsset>>(
                     scope,
                     SelectGraphicAssetFragment<ImageAsset>::assets to projectContext.project?.images!!
                  ).apply {
                     onComplete {
                        val layer = ImageLayer("Layer ${mapVM.layers.size + 1}", it)
                        val command = CreateLayerCommand(mapVM.item, layer)
                        command.execute()
                        layersPane.selectionModel.select(mapVM.layers.size - 1)
                        undoRedoService.push(command, scope)
                     }

                     openModal(block = true, resizable = false)
                  }
               }
            }
         }

         button(graphic = FontIcon("fa-chevron-up")) {
            enableWhen(editorStateVM.selectedLayerIndexProperty.greaterThan(0))
            action {
               val newIndex = editorStateVM.selectedLayerIndex - 1
               val command = MoveLayerCommand(mapVM.item, editorStateVM.selectedLayerIndex, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-chevron-down")) {
            enableWhen(
               editorStateVM.selectedLayerIndexProperty.lessThan(mapVM.layers.sizeProperty().minus(1))
                  .and(editorStateVM.selectedLayerIndexProperty.greaterThanOrEqualTo(0))
            )
            action {
               val newIndex = editorStateVM.selectedLayerIndex + 1
               val command = MoveLayerCommand(mapVM.item, editorStateVM.selectedLayerIndex, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-trash")) {
            enableWhen(editorStateVM.selectedLayerIndexProperty.greaterThanOrEqualTo(0))
            action {
               var index = editorStateVM.selectedLayerIndex
               val command = RemoveLayerCommand(mapVM.item, index)
               command.execute()

               if (--index >= 0) {
                  layersPane.selectionModel.select(index)
               }

               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }
      }
   }

   private class LayerListViewItemConverter(
      private val cell: ListCell<Layer>,
      private val onUpdate: (layer: Layer, name: String) -> Unit
   ) : StringConverter<Layer>() {
      override fun toString(layer: Layer?) = layer?.name ?: ""

      // Disclaimer:
      // Because of the fact that we want to support undo/redo mechanism
      // the actual update must be done from the execute() method of the Command object
      // so that the Command object has access to the actual as well as the new value of layer name.
      // That's why we are running the submission logic in the converter.
      override fun fromString(string: String?): Layer = cell.item.apply {
         string?.takeIf { it != name }?.let {
            onUpdate(this, it)
         }
      }
   }

   private class LayerListViewItem(onUpdate: (layer: Layer, name: String) -> Unit) : TextFieldListCell<Layer>() {
      init {
         converter = LayerListViewItemConverter(this, onUpdate)
      }

      override fun updateItem(item: Layer?, empty: Boolean) {
         super.updateItem(item, empty)

         if (empty || item == null) {
            text = null
            graphic = null
            return
         }

         text = item.name

         graphic = when (item) {
            is TileLayer -> FontIcon("fa-th-large")
            is ObjectLayer -> FontIcon("fa-cube")
            is ColorLayer -> FontIcon("fa-paint-brush")
            is ImageLayer -> FontIcon("fa-image")
            else -> throw IllegalStateException("Unknown layer type")
         }
      }
   }
}
