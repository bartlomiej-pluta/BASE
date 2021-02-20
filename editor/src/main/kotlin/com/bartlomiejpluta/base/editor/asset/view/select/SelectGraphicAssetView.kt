package com.bartlomiejpluta.base.editor.asset.view.select

import com.bartlomiejpluta.base.editor.asset.model.Asset
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import tornadofx.*

class SelectGraphicAssetView<T : Asset> : View() {
   val assets: ObservableList<T> by param()
   val asset: ObjectProperty<T> by param()

   private var assetsListView: ListView<T> by singleAssign()

   private val image = createObjectBinding({
      asset.value?.file?.inputStream()?.use { Image(it) } ?: PLACEHOLDER_IMAGE
   }, asset)

   override val root = hbox {

      assetsListView = listview(assets) {
         cellFormat { text = it.name }
         bindSelected(asset)
      }

      scrollpane {
         prefWidth = 480.0
         prefHeight = 480.0
         imageview(image)
      }
   }

   companion object {
      private val PLACEHOLDER_IMAGE = WritableImage(100, 100)
   }
}