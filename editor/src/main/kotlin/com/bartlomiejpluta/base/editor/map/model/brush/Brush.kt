package com.bartlomiejpluta.base.editor.map.model.brush

import com.bartlomiejpluta.base.editor.tileset.model.Tile
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.asObservable
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue

class Brush {
   val brush: ObservableList<Tile>

   val rowsProperty = SimpleIntegerProperty(0)
   var rows by rowsProperty
      private set

   val columnsProperty = SimpleIntegerProperty(0)
   var columns by columnsProperty
      private set

   val rangeProperty = SimpleIntegerProperty(1)
   var range by rangeProperty
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
         range > 1 || mode == BrushMode.ERASING_MODE -> forEachInRangedBrush(consumer)
         else -> forEachInRegularBrush(consumer)
      }
   }

   private fun forEachInRangedBrush(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) -> Unit) {
      val center = range / 2

      (0 until range).forEach { row ->
         (0 until range).forEach { column ->
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
      this.range = this@Brush.range
      this.mode = this@Brush.mode
   }

   fun withRange(range: Int) = clone().apply {
      this.range = range
   }

   fun withMode(mode: BrushMode) = clone().apply {
      this.mode = mode
   }

   companion object {
      fun of(brushArray: Array<Array<Tile>>) = Brush(brushArray)
   }
}
