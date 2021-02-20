package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue

class ImageLayer(name: String, imageAsset: ImageAsset, mode: ImageLayerMode, opacity: Int) : Layer {
   override val nameProperty = SimpleStringProperty(name)

   override var name by nameProperty

   val imageAssetProperty = SimpleObjectProperty(imageAsset)
   var imageAsset by imageAssetProperty

   val imageProperty = createObjectBinding({
      imageAssetProperty.value.file.inputStream().use { fis -> Image(fis) }
   }, imageAssetProperty)

   val image by imageProperty

   val opacityProperty = SimpleObjectProperty(opacity)
   var opacity by opacityProperty

   val modeProperty = SimpleObjectProperty(mode)
   var mode by modeProperty

   override fun resize(rows: Int, columns: Int) {
      // We essentially need to do nothing
   }
}