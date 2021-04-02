package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.BrushMode
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class PassageAbilityPaintingCursor(
   private val tileWidth: Double,
   private val tileHeight: Double,
   private val editorStateVM: EditorStateVM,
   private val brushVM: BrushVM
) : PaintingCursor {

   override fun render(gc: GraphicsContext) {
      brushVM.forEach { row, column, centerRow, centerColumn, _ ->
         renderTile(gc, row, column, centerRow, centerColumn)
      }
   }

   private fun renderTile(gc: GraphicsContext, row: Int, column: Int, centerRow: Int, centerColumn: Int) {
      val alpha = gc.globalAlpha
      val stroke = gc.stroke
      val width = gc.lineWidth
      gc.globalAlpha = 1.0
      gc.stroke = when (brushVM.mode!!) {
         BrushMode.PAINTING_MODE -> Color.RED
         BrushMode.ERASING_MODE -> Color.WHITE
      }
      gc.lineWidth = 3.0


      val x = tileWidth * (editorStateVM.cursorColumn - centerColumn + column)
      val y = tileHeight * (editorStateVM.cursorRow - centerRow + row)
      gc.strokeLine(x + tileWidth / 2, y + (1 - SIZE) * tileHeight, x + tileWidth / 2, y + SIZE * tileHeight)
      gc.strokeLine(x + (1 - SIZE) * tileWidth, y + tileHeight / 2, x + SIZE * tileWidth, y + tileHeight / 2)

      gc.globalAlpha = alpha
      gc.stroke = stroke
      gc.lineWidth = width
   }

   companion object {
      private const val SIZE = 0.3
   }
}