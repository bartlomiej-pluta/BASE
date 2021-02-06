package com.bartlomiejpluta.base.editor.render.canvas.tileset

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class TileSetCanvas(private val gameMapVM: GameMapVM, private val selection: TileSetSelection) : Renderable, MapMouseEventHandler {
   private var mouseRow = -1
   private var mouseColumn = -1

   override fun render(gc: GraphicsContext) {
      gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

      renderTiles(gc)
      selection.render(gc)
   }

   private fun renderTiles(gc: GraphicsContext) {
      gameMapVM.tileSet.forEach { row, column, tile ->
         gc.fill = if ((row + column) % 2 == 0) BACKGROUND_COLOR1 else BACKGROUND_COLOR2
         gc.fillRect(
            column * tile.image.width,
            row * tile.image.height,
            gameMapVM.tileSet.tileWidth.toDouble(),
            gameMapVM.tileSet.tileHeight.toDouble()
         )
         gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
      }
   }

   override fun handleMouseInput(event: MapMouseEvent) {
      mouseRow = event.row
      mouseColumn = event.column

      when (event.type) {
         MouseEvent.MOUSE_PRESSED -> beginSelection(event)
         MouseEvent.MOUSE_DRAGGED -> proceedSelection(event)
         MouseEvent.MOUSE_RELEASED -> finishSelection(event)
      }
   }

   private fun beginSelection(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         selection.begin(event.row.toDouble(), event.column.toDouble())
      }
   }

   private fun proceedSelection(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         selection.proceed(event.row.toDouble(), event.column.toDouble())
      }
   }

   private fun finishSelection(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         selection.finish(event.row.toDouble(), event.column.toDouble())
      }
   }

   companion object {
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.95, 0.95, 0.95, 0.95)
   }
}