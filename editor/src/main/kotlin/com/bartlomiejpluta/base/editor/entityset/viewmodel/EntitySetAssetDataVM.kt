package com.bartlomiejpluta.base.editor.entityset.viewmodel

import com.bartlomiejpluta.base.editor.entityset.asset.EntitySetAssetData
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class EntitySetAssetDataVM : ItemViewModel<EntitySetAssetData>(EntitySetAssetData()) {
   val nameProperty = bind(EntitySetAssetData::nameProperty)
   var name by nameProperty

   val fileProperty = bind(EntitySetAssetData::fileProperty)
   var file by fileProperty

   val rowsProperty = bind(EntitySetAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(EntitySetAssetData::columnsProperty)
   var columns by columnsProperty
}