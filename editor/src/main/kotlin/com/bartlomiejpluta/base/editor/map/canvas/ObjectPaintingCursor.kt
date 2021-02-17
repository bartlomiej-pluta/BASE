package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

class ObjectPaintingCursor(
   private val tileWidth: Double,
   private val tileHeight: Double,
   private val editorStateVM: EditorStateVM
) : PaintingCursor {

   override fun render(gc: GraphicsContext) {
      val alpha = gc.globalAlpha
      val stroke = gc.stroke
      val width = gc.lineWidth
      gc.globalAlpha = 1.0
      gc.stroke = Color.WHITE
      gc.lineWidth = 3.0

      val x = editorStateVM.cursorColumn * tileWidth
      val y = editorStateVM.cursorRow * tileHeight
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