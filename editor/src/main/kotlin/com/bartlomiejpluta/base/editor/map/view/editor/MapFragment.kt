package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.tileset.view.editor.TileSetView
import javafx.beans.binding.Bindings
import tornadofx.*


class MapFragment : Fragment() {
   override val scope = super.scope as UndoableScope

   private val editorStateVM = find<EditorStateVM>()

   private val mapView = find<MapView>()
   private val layersView = find<MapLayersView>()
   private val tileSetView = find<TileSetView>()
   private val toolbarView = find<MapToolbarView>()
   private val statusBarView = find<MapStatusBarView>()
   private val layerParameters = find<MapLayerParameters>()

   private val isTileLayerSelected = Bindings.createBooleanBinding(
      { editorStateVM.selectedLayer is TileLayer },
      editorStateVM.selectedLayerProperty
   )

   override val root = borderpane {
      top = toolbarView.root

      center = mapView.root

      right = drawer(multiselect = true) {
         item("Layers", expanded = true) {
            this += layersView.root
         }

         item("Tile Set", expanded = true) {
            enableWhen(isTileLayerSelected)

            this += tileSetView.root.apply {
               maxHeightProperty().bind(this@item.heightProperty())
            }
         }

         item("Layer Parameters", expanded = false) {
            this += layerParameters
         }
      }

      bottom = statusBarView.root
   }
}
