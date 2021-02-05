package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import tornadofx.ItemViewModel
import tornadofx.getValue

class GameMapVM : ItemViewModel<GameMap>() {
   val layers: SimpleListProperty<Layer> = bind(GameMap::layers)

   val tileSetProperty = bind(GameMap::tileSet)
   val tileSet by tileSetProperty

   val rowsProperty = bind(GameMap::rows)
   val rows by rowsProperty

   val columnsProperty = bind(GameMap::columns)
   val columns by columnsProperty

   val widthProperty = bind(GameMap::width)
   val width by widthProperty

   val heightProperty = bind(GameMap::height)
   val height by heightProperty

   val selectedLayerProperty = SimpleIntegerProperty(0)
   val selectedLayer by selectedLayerProperty
}



