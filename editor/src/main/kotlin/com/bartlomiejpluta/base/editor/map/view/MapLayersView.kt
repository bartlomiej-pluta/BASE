package com.bartlomiejpluta.base.editor.map.view

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
import javafx.scene.control.TableView
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapLayersView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()

   private val editorStateVM = find<EditorStateVM>()

   private var layersPane = TableView(mapVM.layers).apply {
      column("Layer Name", Layer::nameProperty).makeEditable().setOnEditCommit {
         val command = RenameLayerCommand(it.rowValue, it.newValue)
         command.execute()
         undoRedoService.push(command, scope)
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
}