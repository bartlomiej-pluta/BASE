package com.bartlomiejpluta.base.editor.image.view.importing

import com.bartlomiejpluta.base.editor.image.asset.ImageAssetData
import com.bartlomiejpluta.base.editor.image.viewmodel.ImageAssetDataVM
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

class ImportImageFragment : Fragment("Import Image") {
   private val dataVM = find<ImageAssetDataVM>()
   private val imagePreview = SimpleObjectProperty<Image?>()

   private var onCompleteConsumer: ((ImageAssetData) -> Unit)? = null

   init {
      dataVM.fileProperty.addListener { _, _, file ->
         when (file) {
            null -> imagePreview.value = null
            else -> file.inputStream().use { imagePreview.value = Image(it) }
         }
      }
   }

   fun onComplete(consumer: (ImageAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      prefHeight = 480.0

      fieldset("Import Image") {
         hbox {
            vbox {
               scrollpane {
                  prefWidth = 300.0
                  prefHeightProperty().bind(this@form.heightProperty())
                  imageview(imagePreview)
                  tooltip = tooltip("Click to choose image file")
                  cursor = Cursor.HAND

                  setOnMouseClicked {
                     dataVM.file = chooseFile(
                        title = "Select Image",
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

               field("Image Name") {
                  textfield(dataVM.nameProperty) {
                     required()
                     trimWhitespace()
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