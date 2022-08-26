package com.bartlomiejpluta.base.editor.map.view.editor

import com.bartlomiejpluta.base.editor.autotile.view.editor.AutoTileView
import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.event.RedrawMapRequestEvent
import com.bartlomiejpluta.base.editor.main.component.EditorFragment
import com.bartlomiejpluta.base.editor.main.component.EditorTab.Companion.REDO_SHORTCUT
import com.bartlomiejpluta.base.editor.main.component.EditorTab.Companion.SAVE_SHORTCUT
import com.bartlomiejpluta.base.editor.main.component.EditorTab.Companion.UNDO_SHORTCUT
import com.bartlomiejpluta.base.editor.map.controller.MapController
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.tileset.view.editor.TileSetView
import javafx.beans.binding.Bindings
import javafx.scene.input.KeyEvent
import tornadofx.*


class MapFragment : EditorFragment() {
   override val scope = super.scope as UndoableScope

   private val mapController: MapController by di()
   private val undoRedoService: UndoRedoService by di()

   private val mapVM = find<GameMapVM>()
   private val editorStateVM = find<EditorStateVM>()

   private val mapView = find<MapView>()
   private val layersView = find<MapLayersView>()
   private val tileSetView = find<TileSetView>()
   private val autoTileView = find<AutoTileView>()
   private val toolbarView = find<MapToolbarView>()
   private val statusBarView = find<MapStatusBarView>()
   private val layerParameters = find<MapLayerParameters>()
   private val mapParameters = find<MapParameters>()

   private val isTileLayerSelected = Bindings.createBooleanBinding(
      { editorStateVM.selectedLayer is TileLayer },
      editorStateVM.selectedLayerProperty
   )

   private val isAutoTileLayerSelected = Bindings.createBooleanBinding(
      { editorStateVM.selectedLayer is AutoTileLayer },
      editorStateVM.selectedLayerProperty
   )

   override val root = borderpane {
      top = toolbarView.root

      center = mapView.root

      right = drawer(multiselect = true) {
         maxWidth = 256.0

         item("Layers", expanded = true) {
            this += layersView.root
         }

         item("Tile Set", expanded = true) {
            enableWhen(isTileLayerSelected)

            this += tileSetView.root.apply {
               maxHeightProperty().bind(this@item.heightProperty())
            }
         }

         item("Auto Tile", expanded = false) {
            enableWhen(isAutoTileLayerSelected)

            this += autoTileView.root.apply {
               maxHeightProperty().bind(this@item.heightProperty())
            }
         }

         item("Layer Parameters", expanded = false) {
            this += layerParameters
         }

         item("Map Parameters", expanded = false) {
            this += mapParameters
         }
      }

      bottom = statusBarView.root
   }

   override fun handleShortcut(event: KeyEvent) {
      when {
         SAVE_SHORTCUT.match(event) -> {
            mapController.saveMap(mapVM.item)
            event.consume()
         }

         UNDO_SHORTCUT.match(event) -> {
            undoRedoService.undo(scope)
            fire(RedrawMapRequestEvent)
            event.consume()
         }

         REDO_SHORTCUT.match(event) -> {
            undoRedoService.redo(scope)
            fire(RedrawMapRequestEvent)
            event.consume()
         }
      }
   }
}
