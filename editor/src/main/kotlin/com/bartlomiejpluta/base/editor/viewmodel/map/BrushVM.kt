package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.brush.Brush
import com.bartlomiejpluta.base.editor.model.map.brush.BrushMode
import com.bartlomiejpluta.base.editor.model.tileset.Tile
import tornadofx.ItemViewModel
import tornadofx.getValue

class BrushVM : ItemViewModel<Brush>(Brush.of(arrayOf(arrayOf()))) {
   val brush = bind(Brush::brush)

   val rowsProperty = bind(Brush::rowsProperty)
   val rows by rowsProperty

   val columnsProperty = bind(Brush::columnsProperty)
   val columns by columnsProperty

   val rangeProperty = bind(Brush::rangeProperty)
   val range by rangeProperty

   val modeProperty = bind(Brush::modeProperty)
   val mode by modeProperty

   fun forEach(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) -> Unit) = item.forEach(consumer)

   fun withRange(range: Int) = item.withRange(range)

   fun withMode(mode: BrushMode) = item.withBrushMode(mode)
}