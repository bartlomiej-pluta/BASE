package com.bartlomiejpluta.base.editor.asset.view.select

import com.bartlomiejpluta.base.editor.asset.model.Asset
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.*

class SelectGraphicAssetFragment<T : Asset> : Fragment("Select Asset") {
   val assets: ObservableList<T> by param()

   private val asset = SimpleObjectProperty<T>()

   private val selectGraphicAssetView = find<SelectGraphicAssetView<T>>(
      SelectGraphicAssetView<T>::assets to assets,
      SelectGraphicAssetView<T>::asset to asset
   )

   private var onCompleteConsumer: ((T) -> Unit)? = null

   fun onComplete(onCompleteConsumer: ((T) -> Unit)) {
      this.onCompleteConsumer = onCompleteConsumer
   }

   override val root = form {
      fieldset {
         this += selectGraphicAssetView.root
      }

      buttonbar {
         button("Ok") {
            enableWhen(asset.isNotNull)

            action {
               onCompleteConsumer?.let { it(asset.value) }
               close()
            }
         }

         button("Cancel") {
            action { close() }
         }
      }
   }
}