package com.bartlomiejpluta.base.editor.characterset.viewmodel

import com.bartlomiejpluta.base.editor.characterset.asset.CharacterSetAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class CharacterSetAssetDataVM : ItemViewModel<CharacterSetAssetData>(CharacterSetAssetData()) {
   val nameProperty = bind(CharacterSetAssetData::nameProperty)
   var name by nameProperty

   val fileProperty = bind(CharacterSetAssetData::fileProperty)
   var file by fileProperty

   val rowsProperty = bind(CharacterSetAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(CharacterSetAssetData::columnsProperty)
   var columns by columnsProperty
}