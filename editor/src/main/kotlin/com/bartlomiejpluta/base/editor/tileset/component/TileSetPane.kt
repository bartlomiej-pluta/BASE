package com.bartlomiejpluta.base.editor.tileset.component

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.tileset.canvas.TileSetCanvas
import com.bartlomiejpluta.base.editor.tileset.canvas.TileSetSelection
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent
import tornadofx.doubleBinding

class TileSetPane(editorStateVM: EditorStateVM, private val gameMapVM: GameMapVM, brushVM: BrushVM) : Canvas(),
   EventHandler<MouseEvent> {
   private val selection = TileSetSelection(editorStateVM, gameMapVM, brushVM)
   private val tileSetCanvas = TileSetCanvas(editorStateVM, selection)

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
      if (layer is TileLayer) {
         val tileSet = layer.tileSetProperty.value
         width = tileSet.width.toDouble()
         height = tileSet.height.toDouble()
      } else {
         width = 0.0
         height = 0.0
      }

      render()
   }

   private fun render() {
      tileSetCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         tileSetCanvas.handleMouseInput(MapMouseEvent.of(event, gameMapVM))
      }

      tileSetCanvas.render(graphicsContext2D)
   }
}