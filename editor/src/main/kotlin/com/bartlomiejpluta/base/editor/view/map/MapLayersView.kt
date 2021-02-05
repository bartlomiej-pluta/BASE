package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.control.TableView
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapLayersView : View() {
   private val mapVM = find<GameMapVM>()

   private var layersPane = TableView(mapVM.layers).apply {
      column("Layer Name", Layer::nameProperty).makeEditable()

      mapVM.selectedLayerProperty.bind(selectionModel.selectedIndexProperty())
   }

   override val root = borderpane {
      center = layersPane

      bottom = toolbar {
         button(graphic = FontIcon("fa-plus")) {
            action {
               mapVM.item.createTileLayer("Layer ${mapVM.layers.size + 1}")
               layersPane.selectionModel.select(mapVM.layers.size - 1)
            }
         }

         button(graphic = FontIcon("fa-chevron-up")) {
            enableWhen(mapVM.selectedLayerProperty.greaterThan(0))
            action {
               val newIndex = mapVM.selectedLayer - 1
               mapVM.layers.swap(mapVM.selectedLayer, newIndex)
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
            }
         }

         button(graphic = FontIcon("fa-chevron-down")) {
            enableWhen(
               mapVM.selectedLayerProperty.lessThan(mapVM.layers.sizeProperty().minus(1))
                  .and(mapVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            )
            action {
               val newIndex = mapVM.selectedLayer + 1
               mapVM.layers.swap(mapVM.selectedLayer, newIndex)
               layersPane.selectionModel.select(newIndex)
               fire(RedrawMapRequestEvent)
            }
         }

         button(graphic = FontIcon("fa-trash")) {
            enableWhen(mapVM.selectedLayerProperty.greaterThanOrEqualTo(0))
            action {
               var index = mapVM.selectedLayer
               mapVM.layers.removeAt(index)

               if (--index >= 0) {
                  layersPane.selectionModel.select(index)
               }

               fire(RedrawMapRequestEvent)
            }
         }
      }
   }
}