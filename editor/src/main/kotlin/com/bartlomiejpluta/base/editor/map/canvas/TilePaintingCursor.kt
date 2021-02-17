package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class TilePaintingCursor(
   private val tileWidth: Double,
   private val tileHeight: Double,
   private val editorStateVM: EditorStateVM,
   private val brushVM: BrushVM
) : PaintingCursor {

   override fun render(gc: GraphicsContext) {
      brushVM.forEach { row, column, centerRow, centerColumn, tile ->
         renderTile(gc, row, column, centerRow, centerColumn, tile)
      }
   }

   private fun renderTile(gc: GraphicsContext, row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) {
      val x = tileWidth * (editorStateVM.cursorColumn - centerColumn + column)
      val y = tileHeight * (editorStateVM.cursorRow - centerRow + row)

      when {
         tile != null -> renderPaintingBrushTile(gc, tile, x, y)
         else -> renderEraserTile(gc, x, y)
      }
   }

   private fun renderPaintingBrushTile(gc: GraphicsContext, tile: Tile, x: Double, y: Double) =
      gc.drawImage(tile.image, x, y)

   private fun renderEraserTile(gc: GraphicsContext, x: Double, y: Double) {
      gc.fill = Color.WHITE
      gc.fillRect(x, y, tileWidth, tileHeight)
   }
}