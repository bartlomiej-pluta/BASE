package com.bartlomiejpluta.base.editor.view.map

import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class MapSettingsFragment : Fragment("Map Settings") {
   private val mapVM = find<GameMapVM>()

   var result: Boolean = false
      private set

   override val root = form {
      icon = FontIcon("fa-map-o")

      fieldset("Map Settings") {
         field("Map name") {
            textfield(mapVM.nameProperty) {
               required()
               whenDocked { requestFocus() }
            }
         }

         field("Rows") {

            textfield(mapVM.rowsProperty) {
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
            textfield(mapVM.columnsProperty) {
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

      buttonbar {
         button("Ok") {
            shortcut("Enter")

            action {
               mapVM.commit {
                  result = true
                  close()
               }
            }
         }

         button("Reset") {
            action {
               mapVM.rollback()
            }
         }

         button("Cancel") {
            action { close() }
         }
      }
   }
}