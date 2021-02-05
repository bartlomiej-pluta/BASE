package com.bartlomiejpluta.base.editor.viewmodel.map

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import javafx.collections.ObservableList
import tornadofx.*

class TileSetVM : ItemViewModel<TileSet>() {
   val rowsProperty = bind(TileSet::rowsProperty)
   val rows by rowsProperty

   val columnsProperty = bind(TileSet::columnsProperty)
   val columns by columnsProperty

   val tileWidthProperty = bind(TileSet::tileWidthProperty)
   val tileWidth by tileWidthProperty

   val tileHeightProperty = bind(TileSet::tileHeightProperty)
   val tileHeight by tileHeightProperty

   val widthProperty = bind(TileSet::widthProperty)
   val width by widthProperty

   val heightProperty = bind(TileSet::heightProperty)
   val height by heightProperty

   val tiles: ObservableList<Tile> = bind(TileSet::tiles)

   fun getTile(row: Int, column: Int) = item.getTile(row, column)

   fun getTile(id: Int) = item.getTile(id)

   fun forEach(consumer: (row: Int, column: Int, tile: Tile) -> Unit) = item.forEach(consumer)
}