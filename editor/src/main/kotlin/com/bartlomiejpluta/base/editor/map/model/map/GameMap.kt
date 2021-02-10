package com.bartlomiejpluta.base.editor.map.model.map

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue


class GameMap(val tileSet: TileSet) {
   val layers = observableListOf<Layer>()

   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val tileWidth = tileSet.tileWidth.toDouble()
   val tileHeight = tileSet.tileHeight.toDouble()

   val rowsProperty = SimpleIntegerProperty(INITIAL_ROWS)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(INITIAL_COLUMNS)
   var columns by columnsProperty

   val widthProperty = SimpleDoubleProperty(INITIAL_COLUMNS * tileWidth)
   var width by widthProperty
      private set

   val heightProperty = SimpleDoubleProperty(INITIAL_ROWS * tileHeight)
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

   companion object {
      private const val INITIAL_ROWS = 20
      private const val INITIAL_COLUMNS = 20
   }
}
