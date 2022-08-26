package com.bartlomiejpluta.base.editor.autotile.viewmodel

import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAssetData
import tornadofx.*

class AutoTileAssetDataVM : ItemViewModel<AutoTileAssetData>(AutoTileAssetData()) {
   val nameProperty = bind(AutoTileAssetData::nameProperty)
   var name by nameProperty

   val rowsProperty = bind(AutoTileAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(AutoTileAssetData::columnsProperty)
   var columns by columnsProperty

   val tileWidthProperty = bind(AutoTileAssetData::tileWidthProperty)
   var tileWidth by tileWidthProperty

   val tileHeightProperty = bind(AutoTileAssetData::tileHeightProperty)
   var tileHeight by tileHeightProperty

   val fileProperty = bind(AutoTileAssetData::fileProperty)
   var file by fileProperty
} 