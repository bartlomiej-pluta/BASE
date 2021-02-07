package com.bartlomiejpluta.base.editor.model.map.brush

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.asObservable
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue

class Brush {
   val brush: ObservableList<Tile>

   val rowsProperty = SimpleIntegerProperty(this, "", 0)
   var rows by rowsProperty
      private set

   val columnsProperty = SimpleIntegerProperty(0)
   var columns by columnsProperty
      private set

   val brushRangeProperty = SimpleIntegerProperty(1)
   var brushRange by brushRangeProperty
      private set

   val modeProperty = SimpleObjectProperty(BrushMode.PAINTING_MODE)
   var mode by modeProperty
      private set

   private constructor(brushArray: Array<Array<Tile>>) {
      rowsProperty.value = brushArray.size

      brush = observableListOf()

      brushArray.forEach { brush.addAll(it) }

      if (rowsProperty.value > 0) {
         columns = brush.size / rowsProperty.value
      }
   }

   private constructor(brush: List<Tile>, rows: Int, columns: Int) {
      this.rows = rows
      this.columns = columns

      this.brush = brush.asObservable()
   }

   fun forEach(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) -> Unit) {
      return when {
         brushRange > 1 || mode == BrushMode.ERASING_MODE -> forEachInRangedBrush(consumer)
         else -> forEachInRegularBrush(consumer)
      }
   }

   private fun forEachInRangedBrush(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) -> Unit) {
      val center = brushRange / 2

      (0 until brushRange).forEach { row ->
         (0 until brushRange).forEach { column ->
            consumer(row, column, center, center, getTileByMode(brush[0]))
         }
      }
   }

   private fun getTileByMode(tile: Tile) = when (mode) {
      BrushMode.PAINTING_MODE -> tile
      BrushMode.ERASING_MODE -> null
   }

   private fun forEachInRegularBrush(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) -> Unit) {
      val centerRow = rows / 2
      val centerColumn = columns / 2

      brush.forEachIndexed { id, tile ->
         consumer(id / columns, id % columns, centerRow, centerColumn, getTileByMode(tile))
      }
   }

   private fun clone() = Brush(brush, rows, columns).apply {
      brushRange = this@Brush.brushRange
      mode = this@Brush.mode
   }

   fun withBrushRange(range: Int) = clone().apply {
      brushRange = range
   }

   fun withErasingMode() = clone().apply {
      mode = BrushMode.ERASING_MODE
   }

   fun withPaintingMode() = clone().apply {
      mode = BrushMode.PAINTING_MODE
   }

   companion object {
      fun of(brushArray: Array<Array<Tile>>) = Brush(brushArray)
   }
}
