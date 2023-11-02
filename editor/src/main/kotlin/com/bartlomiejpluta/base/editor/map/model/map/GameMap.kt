package com.bartlomiejpluta.base.editor.map.model.map

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty


class GameMap(val tileWidth: Double, val tileHeight: Double) {
   val uidProperty = SimpleStringProperty()
   var uid by uidProperty

   val layers = FXCollections.observableArrayList(Layer.extractor())

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

   val handlerProperty = SimpleStringProperty()
   var handler by handlerProperty

   val javaImportsProperty = "".toProperty()
   var javaImports by javaImportsProperty

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
