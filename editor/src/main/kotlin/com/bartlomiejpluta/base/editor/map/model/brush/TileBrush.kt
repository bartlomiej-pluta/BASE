package com.bartlomiejpluta.base.editor.map.model.brush

import com.bartlomiejpluta.base.editor.tileset.model.Tile
import tornadofx.observableListOf

class TileBrush(brushArray: Array<Array<Tile>>) : Brush() {
   private val brush: List<Tile>

   private var columns = 0
   private var rows = 0

   init {
      rows = brushArray.size
      brush = observableListOf()
      brushArray.forEach { brush.addAll(it) }
      if (rows > 0) {
         columns = brush.size / rows
      }
   }

   fun forEachTileOnBrush(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) -> Unit) {
      return when {
         range > 1 || mode == BrushMode.ERASING_MODE -> forEachInRange { row, column, centerRow, centerColumn ->
            consumer(row, column, centerRow, centerColumn, brush[0])
         }

         else -> forEachInRegularBrush(consumer)
      }
   }

   private fun getTileByMode(tile: Tile) = when (mode!!) {
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
}