package com.bartlomiejpluta.base.editor.map.model.brush

import com.bartlomiejpluta.base.editor.tileset.model.Tile
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.asObservable
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue

abstract class Brush {
   val modeProperty = SimpleObjectProperty(BrushMode.PAINTING_MODE)
   var mode by modeProperty

   val toolProperty = SimpleObjectProperty(BrushTool.DEFAULT)
   var tool by toolProperty

   val rangeProperty = SimpleIntegerProperty(1)
   var range by rangeProperty

   fun forEachInRange(consumer: (row: Int, column: Int, centerRow: Int, centerColumn: Int) -> Unit) {
      val center = range / 2

      (0 until range).forEach { row ->
         (0 until range).forEach { column ->
            consumer(row, column, center, center)
         }
      }
   }

   companion object {
      fun of(brushArray: Array<Array<Tile>>) = TileBrush(brushArray)
   }
}
