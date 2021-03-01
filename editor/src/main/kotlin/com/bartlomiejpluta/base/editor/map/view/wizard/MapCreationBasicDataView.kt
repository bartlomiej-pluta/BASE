package com.bartlomiejpluta.base.editor.map.view.wizard

import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import tornadofx.*

class MapCreationBasicDataView : View("Basic Data") {
   private val mapBuilderVM = find<GameMapBuilderVM>()

   override val complete = mapBuilderVM.valid(
      mapBuilderVM.nameProperty,
      mapBuilderVM.rowsProperty,
      mapBuilderVM.columnsProperty,
      mapBuilderVM.handlerProperty
   )

   override val root = form {
      fieldset("Map Settings") {
         field("Map name") {
            textfield(mapBuilderVM.nameProperty) {
               required()
               whenDocked { requestFocus() }
            }
         }

         field("Rows") {
            spinner(min = 1, max = 100, property = mapBuilderVM.rowsProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapBuilderVM.rows)

               validator {
                  when (it?.toInt()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
            }
         }

         field("Columns") {
            spinner(min = 1, max = 100, property = mapBuilderVM.columnsProperty, editable = true) {
               required()
               editor.textFormatter = TextFieldUtil.integerFormatter(mapBuilderVM.columns)

               validator {
                  when (it?.toInt()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
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