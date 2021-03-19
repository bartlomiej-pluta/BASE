package com.bartlomiejpluta.base.editor.animation.viewmodel

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class AnimationAssetDataVM : ItemViewModel<AnimationAssetData>(AnimationAssetData()) {
   val nameProperty = bind(AnimationAssetData::nameProperty)
   var name by nameProperty

   val fileProperty = bind(AnimationAssetData::fileProperty)
   var file by fileProperty

   val rowsProperty = bind(AnimationAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(AnimationAssetData::columnsProperty)
   var columns by columnsProperty
}