package com.bartlomiejpluta.base.editor.autotile.viewmodel

import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAssetData
import com.bartlomiejpluta.base.editor.autotile.model.AutoTileLayout
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class AutoTileAssetDataVM : ItemViewModel<AutoTileAssetData>(AutoTileAssetData()) {
   val nameProperty = bind(AutoTileAssetData::nameProperty)
   var name by nameProperty

   val rowsProperty = bind(AutoTileAssetData::rowsProperty)
   var rows by rowsProperty

   val columnsProperty = bind(AutoTileAssetData::columnsProperty)
   var columns by columnsProperty

   val tileSetWidthProperty = bind(AutoTileAssetData::tileSetWidthProperty)
   var tileSetWidth by tileSetWidthProperty

   val tileSetHeightProperty = bind(AutoTileAssetData::tileSetHeightProperty)
   var tileSetHeight by tileSetHeightProperty

   val fileProperty = bind(AutoTileAssetData::fileProperty)
   var file by fileProperty

   val layoutProperty = bind(AutoTileAssetData::layoutProperty)
   var layout by layoutProperty
} 