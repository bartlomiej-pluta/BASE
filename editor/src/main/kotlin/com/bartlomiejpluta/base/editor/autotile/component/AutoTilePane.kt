package com.bartlomiejpluta.base.editor.autotile.component

import com.bartlomiejpluta.base.editor.autotile.canvas.AutoTileCanvas
import com.bartlomiejpluta.base.editor.autotile.canvas.AutoTileSelection
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class AutoTilePane(editorStateVM: EditorStateVM, private val gameMapVM: GameMapVM, brushVM: BrushVM) : Canvas(),
   EventHandler<MouseEvent> {
   private val selection = AutoTileSelection(editorStateVM, gameMapVM, brushVM)
   private val autoTileCanvas = AutoTileCanvas(editorStateVM, gameMapVM, selection)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      updateSize(editorStateVM.selectedLayer)

      editorStateVM.selectedLayerProperty.addListener { _, _, layer ->
         updateSize(layer)
      }

      brushVM.itemProperty.addListener { _, _, _ -> render() }

      render()
   }

   private fun updateSize(layer: Layer?) {
      if (layer is AutoTileLayer) {
         val tileSet = layer.autoTile
         width = (tileSet.tileWidth * tileSet.columns).toDouble()
         height = (tileSet.tileHeight * tileSet.rows).toDouble()
      } else {
         width = 0.0
         height = 0.0
      }

      render()
   }

   private fun render() {
      autoTileCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         autoTileCanvas.handleMouseInput(MapMouseEvent.of(event, gameMapVM))
      }

      autoTileCanvas.render(graphicsContext2D)
   }
}