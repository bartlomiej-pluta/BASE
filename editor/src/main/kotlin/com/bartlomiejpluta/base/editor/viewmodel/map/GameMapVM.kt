package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class GameMapVM : ItemViewModel<GameMap>() {
   val layers: SimpleListProperty<Layer> = bind(GameMap::layers)

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

   val selectedLayerProperty = SimpleIntegerProperty(0)
   val selectedLayer by selectedLayerProperty
}



