package com.bartlomiejpluta.base.editor.model.map.brush

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue

class Brush(newBrush: Array<Array<Tile>>) {
   val brush = observableListOf<Tile>()

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

   init {
      rowsProperty.value = newBrush.size

      newBrush.forEach { brush.addAll(it) }

      if (rowsProperty.value > 0) {
         columns = brush.size / rowsProperty.value
      }

      centerRow = rows / 2
      centerColumn = columns / 2
   }

   fun forEach(consumer: (row: Int, column: Int, tile: Tile) -> Unit) {
      brush.forEachIndexed { id, tile ->
         consumer(id / columns, id % columns, tile)
      }
   }

   fun withBrushRange(range: Int) = Brush(Array(range) { Array(range) { brush[0] } }).apply { brushRange = range }
}
