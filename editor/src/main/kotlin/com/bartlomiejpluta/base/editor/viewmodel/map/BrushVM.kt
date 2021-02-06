package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.brush.Brush
import com.bartlomiejpluta.base.editor.model.tileset.Tile
import tornadofx.*

class BrushVM : ItemViewModel<Brush>(Brush(arrayOf(arrayOf()))) {
   val brush = bind(Brush::brush)

   val rowsProperty = bind(Brush::rowsProperty)
   val rows by rowsProperty

   val columnsProperty = bind(Brush::columnsProperty)
   val columns by columnsProperty

   val centerRowProperty = bind(Brush::centerRowProperty)
   val centerRow by centerRowProperty

   val centerColumnProperty = bind(Brush::centerColumnProperty)
   val centerColumn by centerColumnProperty

   val brushRangeProperty = bind(Brush::brushRangeProperty)
   var brushRange by brushRangeProperty

   fun forEach(consumer: (row: Int, column: Int, tile: Tile) -> Unit) = item.forEach(consumer)

   fun withBrushRange(range: Int) = item.withBrushRange(range)
}