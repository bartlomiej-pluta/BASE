package com.bartlomiejpluta.base.editor.asset.viewmodel

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.ItemViewModel

class GraphicAssetVM(asset: GraphicAsset?) : ItemViewModel<GraphicAsset>(asset) {
   val nameProperty = bind(GraphicAsset::nameProperty)
   val fileProperty = bind(GraphicAsset::file)
   val rowsProperty = bind(GraphicAsset::rowsProperty)
   val columnsProperty = bind(GraphicAsset::columnsProperty)
}