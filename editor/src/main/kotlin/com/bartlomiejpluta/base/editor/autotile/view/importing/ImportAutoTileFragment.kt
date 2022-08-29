package com.bartlomiejpluta.base.editor.autotile.view.importing

import com.bartlomiejpluta.base.editor.asset.component.GraphicAssetViewCanvas
import com.bartlomiejpluta.base.editor.asset.viewmodel.GraphicAssetVM
import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAssetData
import com.bartlomiejpluta.base.editor.autotile.viewmodel.AutoTileAssetDataVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.*

class ImportAutoTileFragment : Fragment("Import Auto Tile") {
   private val dataVM = find<AutoTileAssetDataVM>()
   private val imagePreview = SimpleObjectProperty<Image?>()

   private var onCompleteConsumer: ((AutoTileAssetData) -> Unit)? = null

   private val canvas = GraphicAssetViewCanvas(dataVM.rowsProperty, dataVM.columnsProperty, dataVM.fileProperty)

   init {
      dataVM.fileProperty.addListener { _, _, file ->
         when (file) {
            null -> imagePreview.value = null
            else -> file.inputStream().use { imagePreview.value = Image(it) }
         }
      }

      dataVM.tileSetWidthProperty.addListener { _, _, width ->
         dataVM.columns = (imagePreview.value?.width?.toInt() ?: 1) / width.toInt()
      }

      dataVM.tileSetHeightProperty.addListener { _, _, height ->
         dataVM.rows = (imagePreview.value?.height?.toInt() ?: 1) / height.toInt()
      }

      dataVM.columnsProperty.addListener { _, _, columns ->
         dataVM.tileSetWidth = (imagePreview.value?.width?.toInt() ?: 1) / columns.toInt()
      }

      dataVM.rowsProperty.addListener { _, _, rows ->
         dataVM.tileSetHeight = (imagePreview.value?.height?.toInt() ?: 1) / rows.toInt()
      }

      imagePreview.addListener { _, _, image ->
         dataVM.columns = 1
         dataVM.rows = 1
         dataVM.tileSetWidth = image?.width?.toInt() ?: 0
         dataVM.tileSetHeight = image?.height?.toInt() ?: 0
      }
   }

   fun onComplete(consumer: (AutoTileAssetData) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      prefHeight = 480.0

      fieldset("Import Auto Tile") {
         hbox {
            vbox {
               scrollpane {
                  prefWidth = 300.0
                  prefHeightProperty().bind(this@form.heightProperty())
                  this += canvas
                  tooltip = tooltip("Click to choose Auto Tile file")
                  cursor = Cursor.HAND

                  setOnMouseClicked {
                     dataVM.file = chooseFile(
                        title = "Select Auto Tile",
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

               field("Auto Tile Name") {
                  textfield(dataVM.nameProperty) {
                     required()
                     trimWhitespace()
                  }
               }

               field("Auto Tile Rows") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.rowsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Auto Tile Columns") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.columnsProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.columns)
                  }
               }

               field("Tile Set width") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.tileSetWidthProperty, editable = true) {
                     required()
                     editor.textFormatter = TextFieldUtil.integerFormatter(dataVM.rows)
                  }
               }

               field("Tile Set height") {
                  enableWhen(imagePreview.isNotNull)
                  spinner(min = 1, max = Integer.MAX_VALUE, property = dataVM.tileSetHeightProperty, editable = true) {
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