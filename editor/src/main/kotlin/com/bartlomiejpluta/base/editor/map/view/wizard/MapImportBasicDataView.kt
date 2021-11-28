package com.bartlomiejpluta.base.editor.map.view.wizard

import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import javafx.beans.binding.Bindings
import javafx.stage.FileChooser
import tornadofx.*
import java.io.File

class MapImportBasicDataView : View("Basic Data") {
   private val mapBuilderVM = find<GameMapBuilderVM>()

   override val complete = mapBuilderVM.valid(
      mapBuilderVM.fileProperty,
      mapBuilderVM.nameProperty,
      mapBuilderVM.rowsProperty,
      mapBuilderVM.columnsProperty,
      mapBuilderVM.handlerProperty
   )

   override val root = form {
      fieldset("Map Settings") {

         field("Map file") {
            hbox {
               textfield(mapBuilderVM.fileProperty) {
                  trimWhitespace()
                  whenDocked { requestFocus() }

                  mapBuilderVM.validationContext.addValidator(this, mapBuilderVM.fileProperty) {
                     when {
                        it.isNullOrBlank() -> error("Field is required")
                        !File(it).exists() -> error("Provide valid path to the file")
                        else -> null
                     }
                  }
               }

               button("Choose") {
                  action {
                     mapBuilderVM.fileProperty.value = chooseFile(
                        title = "Map file location",
                        filters = arrayOf(FileChooser.ExtensionFilter("Map files (*.dat)", "*.dat"))
                     ).getOrNull(0)?.absolutePath
                  }
               }
            }
         }

         label("Only tile, object and color layers will be imported. Any image layers will be dropped out.")

         field("Map name") {
            textfield(mapBuilderVM.nameProperty) {
               required()
            }
         }

         field("Map Handler class") {
            textfield(mapBuilderVM.handlerProperty) {
               required()
               trimWhitespace()
            }
         }
      }
   }
}