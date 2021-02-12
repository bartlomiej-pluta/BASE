package com.bartlomiejpluta.base.editor.tileset.view.importing

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetBuilder
import com.bartlomiejpluta.base.editor.tileset.viewmodel.TileSetAssetBuilderVM
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

class ImportTileSetFragment : Fragment("Import Tile Set") {
   private val builderVM = find<TileSetAssetBuilderVM>()
   private val imagePreview = SimpleObjectProperty<Image?>()

   private var onCompleteConsumer: ((TileSetAssetBuilder) -> Unit)? = null

   init {
      builderVM.fileProperty.addListener { _, _, file ->
         when(file) {
            null -> imagePreview.value = null
            else -> file.inputStream().use { imagePreview.value = Image(it) }
         }
      }
   }

   fun onComplete(consumer: (TileSetAssetBuilder) -> Unit) {
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
                     builderVM.file = chooseFile(
                        title = "Select Sprite",
                        filters = arrayOf(FileChooser.ExtensionFilter("PNG Images (*.png)", "*.png"))
                     ).getOrNull(0)
                  }

                  builderVM.validationContext.addValidator(this@vbox, builderVM.fileProperty) {
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
                  textfield(builderVM.nameProperty) {
                     required()
                     trimWhitespace()
                  }
               }

               field("Tile Set Rows") {
                  textfield(builderVM.rowsProperty) {
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
                  textfield(builderVM.columnsProperty) {
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
               if(builderVM.commit()) {
                  onCompleteConsumer?.let { it(builderVM.item) }
                  close()
               }
            }
         }

         button("Reset") {
            action { builderVM.rollback() }
         }


         button("Cancel") {
            action { close() }
         }
      }
   }
}