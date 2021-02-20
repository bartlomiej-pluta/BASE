package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue

class ImageLayer(name: String, imageAsset: ImageAsset, alpha: Int) : Layer {
   override val nameProperty = SimpleStringProperty(name)

   override var name by nameProperty

   val imageAssetProperty = SimpleObjectProperty(imageAsset)
   var imageAsset by imageAssetProperty

   val imageProperty = createObjectBinding({
      imageAssetProperty.value.file.inputStream().use { fis -> Image(fis) }
   }, imageAssetProperty)

   val image by imageProperty

   val alphaProperty = SimpleObjectProperty(alpha)
   var alpha by alphaProperty

   override fun resize(rows: Int, columns: Int) {
      // We essentially need to do nothing
   }
}