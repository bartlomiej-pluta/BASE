package com.bartlomiejpluta.base.editor.render.canvas.tileset

import com.bartlomiejpluta.base.editor.model.map.brush.Brush
import com.bartlomiejpluta.base.editor.render.model.Renderable
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs
import kotlin.math.min


class TileSetSelection(private val gameMapVM: GameMapVM, private val brushVM: BrushVM) : Renderable {
   private var startRow = 0.0
   private var startColumn = 0.0
   private var offsetRow = 0.0
   private var offsetColumn = 0.0
   private var x = 0.0
   private var y = 0.0
   private var width = gameMapVM.tileSet.tileWidth.toDouble()
   private var height = gameMapVM.tileSet.tileHeight.toDouble()


   fun begin(row: Double, column: Double) {
      startRow = row
      offsetRow = 0.0
      startColumn = column
      offsetColumn = 0.0

      updateRect(row, column)
   }

   private fun updateRect(row: Double, column: Double) {
      x = min(column, startColumn) * gameMapVM.tileSet.tileWidth
      y = min(row, startRow) * gameMapVM.tileSet.tileHeight
      width = (offsetColumn + 1) * gameMapVM.tileSet.tileWidth
      height = (offsetRow + 1) * gameMapVM.tileSet.tileHeight
   }

   fun proceed(row: Double, column: Double) {
      offsetRow = abs(row - startRow)
      offsetColumn = abs(column - startColumn)

      updateRect(row, column)
   }

   fun finish(row: Double, column: Double) {
      proceed(row, column)

      startRow = min(row, startRow)
      startColumn = min(column, startColumn)

      val firstRow = startRow.toInt()
      val firstColumn = startColumn.toInt()
      val rows = offsetRow.toInt() + 1
      val columns = offsetColumn.toInt() + 1

      val brushArray = Array(rows) { rowIndex ->
         Array(columns) { columnIndex ->
            gameMapVM.tileSet.getTile(firstRow + rowIndex, firstColumn + columnIndex)
         }
      }

      brushVM.item = Brush.of(brushArray)
   }

   override fun render(gc: GraphicsContext) {
      gc.fill = SELECTION_FILL_COLOR
      gc.fillRect(x, y, width, height)

      gc.stroke = SELECTION_STROKE_COLOR
      gc.strokeRect(x, y, width, height)
   }

   companion object {
      private val SELECTION_FILL_COLOR = Color.color(0.0, 0.7, 1.0, 0.4)
      private val SELECTION_STROKE_COLOR = Color.color(0.0, 0.7, 1.0, 1.0)
   }
}