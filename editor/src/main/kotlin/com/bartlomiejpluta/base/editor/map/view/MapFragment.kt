package com.bartlomiejpluta.base.editor.map.view

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.tileset.view.TileSetView
import tornadofx.*


class MapFragment : Fragment() {
   override val scope = super.scope as UndoableScope

   private val mapView = find<MapView>()
   private val layersView = find<MapLayersView>()
   private val tileSetView = find<TileSetView>()
   private val toolbarView = find<MapToolbarView>()
   private val statusBarView = find<MapStatusBarView>()

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

      bottom = statusBarView.root
   }
}
