package com.bartlomiejpluta.base.editor.model.map.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue


class GameMap(val tileSet: TileSet, initialColumns: Int, initialRows: Int) {
   val layers = observableListOf<Layer>()

   val tileWidth = tileSet.tileWidth.toDouble()
   val tileHeight = tileSet.tileHeight.toDouble()

   val rowsProperty = SimpleIntegerProperty(initialRows)
   val rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(initialColumns)
   val columns by columnsProperty

   val widthProperty = SimpleDoubleProperty(initialColumns * tileWidth)
   var width by widthProperty
      private set

   val heightProperty = SimpleDoubleProperty(initialRows * tileHeight)
   var height by heightProperty
      private set

   init {
      rowsProperty.addListener { _, _, newValue ->
         val newRows = newValue.toInt()
         height = newRows * tileWidth
         layers.forEach { it.resize(newRows, columns) }
      }

      columnsProperty.addListener { _, _, newValue ->
         val newColumns = newValue.toInt()
         width = newColumns * tileWidth
         layers.forEach { it.resize(rows, newColumns) }
      }
   }
}
