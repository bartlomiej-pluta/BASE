package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.model.map.CreateLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.MoveLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.RemoveLayerCommand
import com.bartlomiejpluta.base.editor.command.model.map.RenameLayerCommand
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.viewmodel.map.EditorOptionsVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.control.TableView
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapLayersView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()

   private val editorOptionsVM = find<EditorOptionsVM>()

   private var layersPane = TableView(mapVM.layers).apply {
      column("Layer Name", Layer::nameProperty).makeEditable().setOnEditCommit {
         val command = RenameLayerCommand(it.rowValue, it.newValue)
         command.execute()
         undoRedoService.push(command, scope)
      }

      editorOptionsVM.selectedLayerProperty.bind(selectionModel.selectedIndexProperty())
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
            enableWhen(editorOptionsVM.selectedLayerProperty.greaterThan(0))
            action {
               val newIndex = editorOptionsVM.selectedLayer - 1
               val command = MoveLayerCommand(mapVM.item, editorOptionsVM.selectedLayer, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-chevron-down")) {
            enableWhen(
               editorOptionsVM.selectedLayerProperty.lessThan(mapVM.layers.sizeProperty().minus(1))
                  .and(editorOptionsVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            )
            action {
               val newIndex = editorOptionsVM.selectedLayer + 1
               val command = MoveLayerCommand(mapVM.item, editorOptionsVM.selectedLayer, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-trash")) {
            enableWhen(editorOptionsVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            action {
               var index = editorOptionsVM.selectedLayer
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
}