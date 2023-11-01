package com.bartlomiejpluta.base.editor.common.view

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class StringInputFragment : Fragment("Enter value") {
   val valueProperty = SimpleStringProperty()
   var value by valueProperty

   private var onCompleteConsumer: ((String) -> Unit)? = null

   fun onComplete(consumer: (String) -> Unit) {
      this.onCompleteConsumer = consumer
   }


   override val root = borderpane {
      center = textfield(valueProperty) {
         whenDocked { requestFocus() }
      }

      bottom = buttonbar {
         button("Apply") {
            action {
               onCompleteConsumer?.let { it(value) }
               close()
            }
         }

         button("Cancel") {
            action {
               close()
            }
         }
      }
   }
}