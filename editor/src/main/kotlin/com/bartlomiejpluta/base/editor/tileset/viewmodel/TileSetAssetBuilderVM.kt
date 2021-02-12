package com.bartlomiejpluta.base.editor.tileset.viewmodel

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetBuilder
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class TileSetAssetBuilderVM : ItemViewModel<TileSetAssetBuilder>(TileSetAssetBuilder()) {
   val nameProperty = bind(TileSetAssetBuilder::nameProperty)
   var name by nameProperty

   val rowsProperty = bind(TileSetAssetBuilder::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(TileSetAssetBuilder::columnsProperty)
   var columns by columnsProperty

   val fileProperty = bind(TileSetAssetBuilder::fileProperty)
   var file by fileProperty
}