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

   val columnsProperty = SimpleIntegerProperty(0)
   var columns by columnsProperty

   val centerRowProperty = SimpleIntegerProperty(0)
   var centerRow by centerRowProperty

   val centerColumnProperty = SimpleIntegerProperty(0)
   var centerColumn by centerColumnProperty

   val brushRangeProperty = SimpleIntegerProperty(1)
   var brushRange by brushRangeProperty

   val modeProperty = SimpleObjectProperty(BrushMode.PAINTING_MODE)
   var mode by modeProperty

   private constructor(brushArray: Array<Array<Tile>>) {
      rowsProperty.value = brushArray.size

      brush = observableListOf()

      brushArray.forEach { brush.addAll(it) }

      if (rowsProperty.value > 0) {
         columns = brush.size / rowsProperty.value
      }

      centerRow = rows / 2
      centerColumn = columns / 2
   }

   private constructor(brush: List<Tile>, rows: Int, columns: Int) {
      this.rows = rows
      this.columns = columns

      this.brush = brush.asObservable()

      centerRow = rows / 2
      centerColumn = columns / 2
   }

   fun forEach(consumer: (row: Int, column: Int, tile: Tile?) -> Unit) {
      brush.forEachIndexed { id, tile ->
         consumer(id / columns, id % columns, when(mode) {
            BrushMode.PAINTING_MODE -> tile
            BrushMode.ERASING_MODE -> null
         })
      }
   }

   fun withBrushRange(range: Int) = Brush(Array(range) { Array(range) { brush[0] } }).apply {
      brushRange = range
      mode = this@Brush.mode
   }

   fun withErasingMode() = Brush(brush, rows, columns).apply {
      brushRange = this@Brush.brushRange
      mode = BrushMode.ERASING_MODE
   }

   fun withPaintingMode() = Brush(brush, rows, columns).apply {
      brushRange = this@Brush.brushRange
      mode = BrushMode.PAINTING_MODE
   }

   companion object {
      fun of(brushArray: Array<Array<Tile>>) = Brush(brushArray)
   }
}
