package com.bartlomiejpluta.base.editor.asset.view.select

import com.bartlomiejpluta.base.editor.asset.model.Asset
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
import tornadofx.*

class SelectGraphicAssetFragment : Fragment("Select Asset") {
   val assets: ObservableList<Asset> by param()
   val asset: ObjectProperty<Asset> by param()

   private val selectGraphicAssetView = find<SelectGraphicAssetView>(
      SelectGraphicAssetView::assets to assets,
      SelectGraphicAssetView::asset to asset
   )

   private var onCompleteConsumer: ((Asset) -> Unit)? = null

   fun onComplete(onCompleteConsumer: ((Asset) -> Unit)) {
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