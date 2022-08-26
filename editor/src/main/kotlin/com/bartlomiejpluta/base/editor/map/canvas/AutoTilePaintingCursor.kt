package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class AutoTilePaintingCursor(
   private val tileWidth: Double,
   private val tileHeight: Double,
   private val editorStateVM: EditorStateVM,
   private val brushVM: BrushVM
) : PaintingCursor {

   override fun render(gc: GraphicsContext) {
      brushVM.item.forEachInRange { row, column, centerRow, centerColumn ->
         renderTile(gc, row, column, centerRow, centerColumn)
      }
   }

   private fun renderTile(gc: GraphicsContext, row: Int, column: Int, centerRow: Int, centerColumn: Int) {
      val alpha = gc.globalAlpha
      val stroke = gc.stroke
      val width = gc.lineWidth
      gc.globalAlpha = 1.0
      gc.stroke = when (brushVM.mode!!) {
         BrushMode.PAINTING_MODE -> Color.WHITE
         BrushMode.ERASING_MODE -> Color.RED
      }
      gc.lineWidth = 3.0

      val x = tileWidth * (editorStateVM.cursorColumn - centerColumn + column)
      val y = tileHeight * (editorStateVM.cursorRow - centerRow + row)

      val centerX = x + tileWidth / 2
      val topY = y + (1 - SIZE) * tileHeight
      val bottomY = y + SIZE * tileHeight

      val centerY = y + tileHeight / 2
      val leftX = x + (1 - SIZE) * tileWidth
      val rightX = x + SIZE * tileWidth

      gc.strokeLine(centerX, topY, rightX, centerY)
      gc.strokeLine(rightX, centerY, centerX, bottomY)
      gc.strokeLine(centerX, bottomY, leftX, centerY)
      gc.strokeLine(leftX, centerY, centerX, topY)

      gc.globalAlpha = alpha
      gc.stroke = stroke
      gc.lineWidth = width
   }

   companion object {
      private const val SIZE = 0.3
   }
}