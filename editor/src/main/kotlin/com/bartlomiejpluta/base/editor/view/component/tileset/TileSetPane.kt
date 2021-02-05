package com.bartlomiejpluta.base.editor.view.component.tileset

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.tileset.TileSetCanvas
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class TileSetPane(private val gameMapVM: GameMapVM, brushVM: BrushVM) : Canvas(), EventHandler<MouseEvent> {
   private val tileSetCanvas = TileSetCanvas(gameMapVM, brushVM)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      width = gameMapVM.tileSet.width.toDouble()
      height = gameMapVM.tileSet.height.toDouble()

      render()
   }

   private fun render() {
      tileSetCanvas.render(graphicsContext2D)
   }

   override fun handle(event: MouseEvent?) {
      if (event != null) {
         tileSetCanvas.handleMouseInput(MapMouseEvent.of(event, gameMapVM.tileSet))
      }

      tileSetCanvas.render(graphicsContext2D)
   }
}