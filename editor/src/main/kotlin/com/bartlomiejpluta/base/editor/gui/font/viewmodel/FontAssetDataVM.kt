package com.bartlomiejpluta.base.editor.gui.font.viewmodel

import com.bartlomiejpluta.base.editor.gui.font.asset.FontAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class FontAssetDataVM : ItemViewModel<FontAssetData>(FontAssetData()) {
   val nameProperty = bind(FontAssetData::nameProperty)
   var name by nameProperty

   val fileProperty = bind(FontAssetData::fileProperty)
   var file by fileProperty
}