package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.map.map.MapProperty
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import tornadofx.*

class GameMapVM : ItemViewModel<GameMap>() {
   val layers: SimpleListProperty<Layer> = bind(GameMap::layers)
   val mapProperties: SimpleListProperty<MapProperty> = bind(GameMap::mapProperties)

   val tileSetProperty = bind(GameMap::tileSet)
   val tileSet by tileSetProperty

   val rowsProperty = bind(GameMap::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(GameMap::columnsProperty)
   var columns by columnsProperty

   val tileWidthProperty = bind(GameMap::tileWidth)
   val tileWidth by tileWidthProperty

   val tileHeightProperty = bind(GameMap::tileHeight)
   val tileHeight by tileHeightProperty

   val widthProperty = bind(GameMap::widthProperty)
   val width by widthProperty

   val heightProperty = bind(GameMap::heightProperty)
   val height by heightProperty
//   val widthProperty = ReadOnlyDoubleWrapper().apply { bind(tileSetProperty.getProperty(TileSet::tileWidthProperty)) }
//   val width by widthProperty
//
//   val heightProperty = ReadOnlyDoubleWrapper().apply { bind(rowsProperty.multiply(32.0)) }
//   val height by heightProperty

   val selectedLayerProperty = SimpleIntegerProperty(0)
   val selectedLayer by selectedLayerProperty
}



