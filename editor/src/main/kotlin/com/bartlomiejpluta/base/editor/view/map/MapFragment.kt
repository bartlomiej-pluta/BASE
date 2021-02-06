package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import tornadofx.*


class MapFragment : Fragment() {
   override val scope = super.scope as UndoableScope

   val map: GameMap by param()

   private val mapVM = find<GameMapVM>().apply { item = map }

   private val mapView = find<MapView>()
   private val layersView = find<MapLayersView>()
   private val tileSetView = find<TileSetView>()
   private val toolbarView = find<MapToolbarView>()


   override val root = borderpane {
      top = toolbarView.root

      center = mapView.root

      right = drawer(multiselect = true) {
         item("Layers", expanded = true) {
            this += layersView.root
         }

         item("Tile Set", expanded = true) {
            this += tileSetView.root.apply {
               maxHeightProperty().bind(this@item.heightProperty())
            }
         }
      }
   }
}
