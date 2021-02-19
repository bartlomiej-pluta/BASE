package com.bartlomiejpluta.base.editor.image.viewmodel

import com.bartlomiejpluta.base.editor.image.asset.ImageAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class ImageAssetDataVM : ItemViewModel<ImageAssetData>(ImageAssetData()) {
   val nameProperty = bind(ImageAssetData::nameProperty)
   var name by nameProperty

   val fileProperty = bind(ImageAssetData::fileProperty)
   var file by fileProperty
}