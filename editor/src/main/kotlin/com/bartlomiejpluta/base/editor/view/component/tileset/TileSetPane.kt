package com.bartlomiejpluta.base.editor.view.component.tileset

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.tileset.TileSetCanvas
import com.bartlomiejpluta.base.editor.render.canvas.tileset.TileSetSelection
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class TileSetPane(private val gameMapVM: GameMapVM, brushVM: BrushVM) : Canvas(), EventHandler<MouseEvent> {
   private val selection = TileSetSelection(gameMapVM, brushVM)
   private val tileSetCanvas = TileSetCanvas(gameMapVM, selection)

   init {
      onMouseMoved = this
      onMouseDragged = this
      onMousePressed = this
      onMouseReleased = this

      width = gameMapVM.tileSet.width.toDouble()
      height = gameMapVM.tileSet.height.toDouble()

      // Shrink the selection just one tile (the top left one)
      // when brush range (size) is increased to 2 or more
      // (because the range-increased brush can only include
      // the tile of one type).
      brushVM.itemProperty.addListener { _, _, brush ->
         if (brush.brushRange > 1) {
            selection.shrinkToTopLeftTile()
         }

         render()
      }

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