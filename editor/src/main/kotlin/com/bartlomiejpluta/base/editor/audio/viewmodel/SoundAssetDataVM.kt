package com.bartlomiejpluta.base.editor.audio.viewmodel

import com.bartlomiejpluta.base.editor.audio.asset.SoundAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class SoundAssetDataVM : ItemViewModel<SoundAssetData>(SoundAssetData()) {
   val nameProperty = bind(SoundAssetData::nameProperty)
   var name by nameProperty

   val fileProperty = bind(SoundAssetData::fileProperty)
   var file by fileProperty
}