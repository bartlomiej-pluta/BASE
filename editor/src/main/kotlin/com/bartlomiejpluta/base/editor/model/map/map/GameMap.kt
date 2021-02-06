package com.bartlomiejpluta.base.editor.model.map.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import tornadofx.observableListOf


class GameMap(val tileSet: TileSet, val rows: Int, val columns: Int) {
   val layers = observableListOf<Layer>()

   val width = columns * tileSet.tileWidth

   val height = columns * tileSet.tileWidth
}
