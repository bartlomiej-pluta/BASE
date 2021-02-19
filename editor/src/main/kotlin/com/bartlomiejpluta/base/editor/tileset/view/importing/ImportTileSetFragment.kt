package com.bartlomiejpluta.base.editor.tileset.view.importing

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetData
import com.bartlomiejpluta.base.editor.tileset.viewmodel.TileSetAssetDataVM
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

class ImportTileSetFragment : Fragment("Import Tile Set") {
   private val dataVM = find<TileSetAssetDataVM>()
   private val imagePreview = SimpleObjectProperty<Image?>()

   private var onCompleteConsumer: ((TileSetAssetData) -> Unit)? = null

   init {
      dataVM.fileProperty.addListener { _, _, file ->
         when (file) {
            null -> imagePreview.value = null
            else -> file.inputStream().use { imagePreview.value = Image(it) }
         }
      }
   }

   fun onComplete(consumer: (TileSetAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      prefHeight = 480.0

      fieldset("Import Tile Set") {
         hbox {
            vbox {
               scrollpane {
                  prefWidth = 300.0
                  prefHeightProperty().bind(this@form.heightProperty())
                  imageview(imagePreview)
                  tooltip = tooltip("Click to choose sprite file")
                  cursor = Cursor.HAND

                  setOnMouseClicked {
                     dataVM.file = chooseFile(
                        title = "Select Sprite",
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

               field("Tile Set Name") {
                  textfield(dataVM.nameProperty) {
                     required()
                     trimWhitespace()
                  }
               }

               field("Tile Set Rows") {
                  textfield(dataVM.rowsProperty) {
                     stripNonInteger()
                     required()
                     trimWhitespace()

                     validator {
                        val value = it?.toIntOrNull()
                        when {
                           value == null -> error("This field is required")
                           value < 1 -> error("The value must not be lower than 1")
                           else -> null
                        }
                     }
                  }
               }

               field("Tile Set Columns") {
                  textfield(dataVM.columnsProperty) {
                     stripNonInteger()
                     required()
                     trimWhitespace()

                     validator {
                        val value = it?.toIntOrNull()
                        when {
                           value == null -> error("This field is required")
                           value < 1 -> error("The value must not be lower than 1")
                           else -> null
                        }
                     }
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