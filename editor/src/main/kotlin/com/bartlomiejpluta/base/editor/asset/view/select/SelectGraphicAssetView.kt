package com.bartlomiejpluta.base.editor.asset.view.select

import com.bartlomiejpluta.base.editor.asset.component.GraphicAssetPreviewView
import com.bartlomiejpluta.base.editor.asset.component.GraphicAssetViewCanvas
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.asset.viewmodel.GraphicAssetVM
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import tornadofx.*

class SelectGraphicAssetView<T : GraphicAsset> : View() {
   val assets: ObservableList<T> by param()
   val asset: ObjectProperty<T> by param()

   val vm = GraphicAssetVM(null)

   private var assetsListView: ListView<T> by singleAssign()

   init {
      asset.addListener { _, _, item -> vm.item = item }
   }

   override val root = hbox {

      assetsListView = listview(assets) {
         cellFormat { text = it.name }
         bindSelected(asset)
      }

      scrollpane {
         prefWidth = 480.0
         prefHeight = 480.0
         this += GraphicAssetViewCanvas(vm)
      }
   }

   companion object {
      private val PLACEHOLDER_IMAGE = WritableImage(100, 100)
   }
}