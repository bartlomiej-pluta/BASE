package com.bartlomiejpluta.base.editor.model.map.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.ReadOnlyFloatProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*


class GameMap(val tileSet: TileSet, initialColumns: Int, initialRows: Int) {
   val layers = observableListOf<Layer>()
   val mapProperties = observableListOf<MapProperty>()


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
         height = newValue.toInt() * tileWidth
      }

      columnsProperty.addListener { _, _, newValue ->
         width = newValue.toInt() * tileWidth
      }
   }
}
