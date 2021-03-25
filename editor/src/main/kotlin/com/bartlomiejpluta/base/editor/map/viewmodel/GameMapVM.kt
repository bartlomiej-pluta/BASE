package com.bartlomiejpluta.base.editor.map.viewmodel

import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import javafx.beans.property.SimpleListProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class GameMapVM(map: GameMap) : ItemViewModel<GameMap>(map) {
   val layers: SimpleListProperty<Layer> = bind(GameMap::layers)

   val uidProperty = bind(GameMap::uidProperty)
   val uid by uidProperty

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

   val handlerProperty = bind(GameMap::handlerProperty)
   var handler by handlerProperty
}



