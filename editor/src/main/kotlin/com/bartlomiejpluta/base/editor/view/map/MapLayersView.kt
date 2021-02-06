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
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.control.TableView
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapLayersView : View() {
   private val undoRedoService: UndoRedoService by di()

   override val scope = super.scope as UndoableScope

   private val mapVM = find<GameMapVM>()

   private var layersPane = TableView(mapVM.layers).apply {
      column("Layer Name", Layer::nameProperty).makeEditable().setOnEditCommit {
         val command = RenameLayerCommand(it.rowValue, it.newValue)
         command.execute()
         undoRedoService.push(command, scope)
      }

      mapVM.selectedLayerProperty.bind(selectionModel.selectedIndexProperty())
   }

   override val root = borderpane {
      center = layersPane

      bottom = toolbar {
         button(graphic = FontIcon("fa-plus")) {
            action {
               val tileLayer = TileLayer.empty("Layer ${mapVM.layers.size + 1}", mapVM.rows, mapVM.columns)
               val command = CreateLayerCommand(mapVM.item, tileLayer)
               command.execute()
               layersPane.selectionModel.select(mapVM.layers.size - 1)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-chevron-up")) {
            enableWhen(mapVM.selectedLayerProperty.greaterThan(0))
            action {
               val newIndex = mapVM.selectedLayer - 1
               val command = MoveLayerCommand(mapVM.item, mapVM.selectedLayer, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-chevron-down")) {
            enableWhen(
               mapVM.selectedLayerProperty.lessThan(mapVM.layers.sizeProperty().minus(1))
                  .and(mapVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            )
            action {
               val newIndex = mapVM.selectedLayer + 1
               val command = MoveLayerCommand(mapVM.item, mapVM.selectedLayer, newIndex)
               command.execute()
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
               undoRedoService.push(command, scope)
            }
         }

         button(graphic = FontIcon("fa-trash")) {
            enableWhen(mapVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            action {
               var index = mapVM.selectedLayer
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