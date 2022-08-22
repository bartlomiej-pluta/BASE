package com.bartlomiejpluta.base.editor.iconset.view.importing

import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAssetData
import com.bartlomiejpluta.base.editor.iconset.viewmodel.IconSetAssetDataVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

class ImportIconSetFragment : Fragment("Import Icon Set") {
   private val dataVM = find<IconSetAssetDataVM>()
   private val imagePreview = SimpleObjectProperty<Image?>()

   private var onCompleteConsumer: ((IconSetAssetData) -> Unit)? = null

   init {
      dataVM.fileProperty.addListener { _, _, file ->
         when (file) {
            null -> imagePreview.value = null
            else -> file.inputStream().use { imagePreview.value = Image(it) }
         }
      }

      dataVM.iconWidthProperty.addListener { _, _, width ->
         dataVM.columns = (imagePreview.value?.width?.toInt() ?: 1) / width.toInt()
      }

      dataVM.iconHeightProperty.addListener { _, _, height ->
         dataVM.rows = (imagePreview.value?.height?.toInt() ?: 1) / height.toInt()
      }

      dataVM.columnsProperty.addListener { _, _, columns ->
         dataVM.iconWidth = (imagePreview.value?.width?.toInt() ?: 1) / columns.toInt()
      }

      dataVM.rowsProperty.addListener { _, _, rows ->
         dataVM.iconHeight = (imagePreview.value?.height?.toInt() ?: 1) / rows.toInt()
      }

      imagePreview.addListener { _, _, _ ->
         dataVM.columns = 1
         dataVM.rows = 1
      }
   }

   fun onComplete(consumer: (IconSetAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      prefHeight = 480.0

      fieldset("Import Icon Set") {
         hbox {
            vbox {
               scrollpane {
                  prefWidth = 300.0
                  prefHeightProperty().bind(this@form.heightProperty())
                  imageview(imagePreview)
                  tooltip = tooltip("Click to choose Icon Set file")
                  cursor = Cursor.HAND

                  setOnMouseClicked {
                     dataVM.file = chooseFile(
                        title = "Select Icon Set",
                        filters = arrayOf(FileChooser.ExtensionFilter("PNG Images (*.png)", "*.png"))
                     ).getOrNull(0)
                  }

                  dataVM.validationContext.addValidator(this@vbox, dataVM.fileProperty) {
                     when {
                        it == null -> error("This field is required")
                        !it.exists() -> error("The file must exist")
                        else -> null
                     }
                  }
               }
            }

            vbox {
               paddingLeft = 20.0

               field("Icon Set Name") {
                  textfield(dataVM.nameProperty) {
                     required()
                     trimWhitespace()
                  }
               }

               field("Icon Set Rows") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.rowsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Icon Set Columns") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.columnsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.columns)
                  }
               }

               field("Icon width") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.iconWidthProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Icon height") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.iconHeightProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.columns)
                  }
               }
            }
         }
      }

      buttonbar {
         button("Import") {
            action {
               if (dataVM.commit()) {
                  onCompleteConsumer?.let { it(dataVM.item) }
                  close()
               }
            }
         }

         button("Reset") {
            action { dataVM.rollback() }
         }


         button("Cancel") {
            action { close() }
         }
      }
   }
}