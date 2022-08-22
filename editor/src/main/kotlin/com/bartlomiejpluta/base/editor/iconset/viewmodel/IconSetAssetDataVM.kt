package com.bartlomiejpluta.base.editor.iconset.viewmodel

import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class IconSetAssetDataVM : ItemViewModel<IconSetAssetData>(IconSetAssetData()) {
   val nameProperty = bind(IconSetAssetData::nameProperty)
   var name by nameProperty

   val rowsProperty = bind(IconSetAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(IconSetAssetData::columnsProperty)
   var columns by columnsProperty

   val iconWidthProperty = bind(IconSetAssetData::iconWidthProperty)
   var iconWidth by iconWidthProperty

   val iconHeightProperty = bind(IconSetAssetData::iconHeightProperty)
   var iconHeight by iconHeightProperty

   val fileProperty = bind(IconSetAssetData::fileProperty)
   var file by fileProperty
}