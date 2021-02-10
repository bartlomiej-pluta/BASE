package com.bartlomiejpluta.base.editor.map.view

import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapBuilderVM
import tornadofx.*

class MapCreationBasicDataView : View("Basic Data") {
   private val mapBuilderVM = find<GameMapBuilderVM>()

   override val complete = mapBuilderVM.valid(mapBuilderVM.nameProperty, mapBuilderVM.rowsProperty, mapBuilderVM.columnsProperty)

   override val root = form {
      fieldset("Map Settings") {
         field("Map name") {
            textfield(mapBuilderVM.nameProperty) {
               required()
               whenDocked { requestFocus() }
            }
         }

         field("Rows") {

            textfield(mapBuilderVM.rowsProperty) {
               stripNonInteger()
               required()
               validator {
                  when (it?.toIntOrNull()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
            }
         }

         field("Columns") {
            textfield(mapBuilderVM.columnsProperty) {
               stripNonInteger()
               required()
               validator {
                  when (it?.toIntOrNull()) {
                     in 1..50 -> null
                     in 50..100 -> warning("The map sizes over 50 can impact game performance")
                     else -> error("The map size must be between 1 and 100")
                  }
               }
            }
         }
      }
   }
}