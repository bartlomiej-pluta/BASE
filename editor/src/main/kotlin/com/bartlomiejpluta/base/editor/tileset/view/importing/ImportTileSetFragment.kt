package com.bartlomiejpluta.base.editor.tileset.view.importing

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetData
import com.bartlomiejpluta.base.editor.tileset.viewmodel.TileSetAssetDataVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
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

      dataVM.tileWidthProperty.addListener { _, _, width ->
         dataVM.columns = (imagePreview.value?.width?.toInt() ?: 1) / width.toInt()
      }

      dataVM.tileHeightProperty.addListener { _, _, height ->
         dataVM.rows = (imagePreview.value?.height?.toInt() ?: 1) / height.toInt()
      }

      dataVM.columnsProperty.addListener { _, _, columns ->
         dataVM.tileWidth = (imagePreview.value?.width?.toInt() ?: 1) / columns.toInt()
      }

      dataVM.rowsProperty.addListener { _, _, rows ->
         dataVM.tileHeight = (imagePreview.value?.height?.toInt() ?: 1) / rows.toInt()
      }

      imagePreview.addListener { _, _, _ ->
         dataVM.columns = 1
         dataVM.rows = 1
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
                  tooltip = tooltip("Click to choose Tile Set file")
                  cursor = Cursor.HAND

                  setOnMouseClicked {
                     dataVM.file = chooseFile(
                        title = "Select Tile Set",
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
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.rowsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Tile Set Columns") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.columnsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.columns)
                  }
               }

               field("Tile width") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.tileWidthProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Tile height") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.tileHeightProperty, editable = true) {
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