package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty

class ImageLayer(
   name: String,
   imageAsset: ImageAsset,
   opacity: Int = 100,
   x: Int = 0,
   y: Int = 0,
   scaleX: Double = 1.0,
   scaleY: Double = 1.0,
   mode: ImageLayerMode = ImageLayerMode.NORMAL,
   parallax: Boolean = false
) : Layer {
   override val nameProperty = SimpleStringProperty(name)

   override var name by nameProperty

   val imageAssetProperty = imageAsset.toProperty()
   var imageAsset by imageAssetProperty

   val imageProperty = createObjectBinding({
      imageAssetProperty.value.file.inputStream().use { fis -> Image(fis) }
   }, imageAssetProperty)

   val image by imageProperty

   val opacityProperty = opacity.toProperty()
   var opacity by opacityProperty

   val xProperty = x.toProperty()
   var x by xProperty

   val yProperty = y.toProperty()
   var y by yProperty

   val scaleXProperty = scaleX.toProperty()
   var scaleX by scaleXProperty

   val scaleYProperty = scaleY.toProperty()
   var scaleY by scaleYProperty

   val modeProperty = mode.toProperty()
   var mode by modeProperty

   val parallaxProperty = parallax.toProperty()
   var parallax by parallaxProperty

   override fun resize(rows: Int, columns: Int) {
      // We essentially need to do nothing
   }
}