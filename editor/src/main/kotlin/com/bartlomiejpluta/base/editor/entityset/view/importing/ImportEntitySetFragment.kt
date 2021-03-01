package com.bartlomiejpluta.base.editor.entityset.view.importing

import com.bartlomiejpluta.base.editor.entityset.asset.EntitySetAssetData
import com.bartlomiejpluta.base.editor.entityset.viewmodel.EntitySetAssetDataVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

class ImportEntitySetFragment : Fragment("Import Entity Set") {
   private val dataVM = find<EntitySetAssetDataVM>()
   private val imagePreview = SimpleObjectProperty<Image?>()

   private var onCompleteConsumer: ((EntitySetAssetData) -> Unit)? = null

   init {
      dataVM.fileProperty.addListener { _, _, file ->
         when (file) {
            null -> imagePreview.value = null
            else -> file.inputStream().use { imagePreview.value = Image(it) }
         }
      }
   }

   fun onComplete(consumer: (EntitySetAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      prefHeight = 480.0

      fieldset("Import Entity Set") {
         hbox {
            vbox {
               scrollpane {
                  prefWidth = 300.0
                  prefHeightProperty().bind(this@form.heightProperty())
                  imageview(imagePreview)
                  tooltip = tooltip("Click to choose Entity Set file")
                  cursor = Cursor.HAND

                  setOnMouseClicked {
                     dataVM.file = chooseFile(
                        title = "Select Entity Set",
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

               field("Entity Set Name") {
                  textfield(dataVM.nameProperty) {
                     required()
                     trimWhitespace()
                  }
               }

               field("Entity Set Rows") {
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.rowsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Entity Set Columns") {
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.columnsProperty, editable = true) {
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