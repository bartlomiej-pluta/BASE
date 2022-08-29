package com.bartlomiejpluta.base.editor.asset.view.select

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.StringProperty
import javafx.collections.ObservableList
import tornadofx.*

class SelectGraphicAssetFragment<T : GraphicAsset> : Fragment("Select Asset") {
   val assets: ObservableList<T> by param()
   val cancelable: BooleanProperty by param(true.toProperty())
   val comment: StringProperty by param("".toProperty())

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
      if (comment.isNotEmpty.value) {
         label(comment) {
            visibleWhen(comment.isNotEmpty)
         }
      }

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
            visibleWhen(cancelable)
            action { close() }
         }
      }
   }
}