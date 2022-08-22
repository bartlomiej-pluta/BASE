package com.bartlomiejpluta.base.editor.tileset.viewmodel

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class TileSetAssetDataVM : ItemViewModel<TileSetAssetData>(TileSetAssetData()) {
   val nameProperty = bind(TileSetAssetData::nameProperty)
   var name by nameProperty

   val rowsProperty = bind(TileSetAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(TileSetAssetData::columnsProperty)
   var columns by columnsProperty

   val tileWidthProperty = bind(TileSetAssetData::tileWidthProperty)
   var tileWidth by tileWidthProperty

   val tileHeightProperty = bind(TileSetAssetData::tileHeightProperty)
   var tileHeight by tileHeightProperty

   val fileProperty = bind(TileSetAssetData::fileProperty)
   var file by fileProperty
}