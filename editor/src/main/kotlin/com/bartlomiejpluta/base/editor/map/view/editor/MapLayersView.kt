package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.model.map.CreateLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.MoveLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.RemoveLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.RenameLayerCommand
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import javafx.beans.value.ObservableValue
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.cell.TextFieldListCell
import javafx.util.StringConverter
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapLayersView : View() {
   private val undoRedoService: UndoRedoService by di()

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

      editorStateVM.selectedLayerProperty.bind(selectionModel.selectedIndexProperty())
   }

   override val root = borderpane {
      center = layersPane

      bottom = toolbar {
         button(graphic = FontIcon("fa-plus")) {
            action {
               val tileLayer = TileLayer("Layer ${mapVM.layers.size + 1}", mapVM.rows, mapVM.columns)
               val command = CreateLayerCommand(mapVM.item, tileLayer)
               command.execute()
               layersPane.selectionModel.select(mapVM.layers.size - 1)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-chevron-up")) {
            enableWhen(editorStateVM.selectedLayerProperty.greaterThan(0))
            action {
               val newIndex = editorStateVM.selectedLayer - 1
               val command = MoveLayerCommand(mapVM.item, editorStateVM.selectedLayer, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-chevron-down")) {
            enableWhen(
               editorStateVM.selectedLayerProperty.lessThan(mapVM.layers.sizeProperty().minus(1))
                  .and(editorStateVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            )
            action {
               val newIndex = editorStateVM.selectedLayer + 1
               val command = MoveLayerCommand(mapVM.item, editorStateVM.selectedLayer, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-trash")) {
            enableWhen(editorStateVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            action {
               var index = editorStateVM.selectedLayer
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
            is TileLayer -> FontIcon("fa-th")
            else -> throw IllegalStateException("Unknown layer type")
         }
      }
   }
}
