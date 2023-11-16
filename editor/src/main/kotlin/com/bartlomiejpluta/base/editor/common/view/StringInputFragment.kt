package com.bartlomiejpluta.base.editor.common.view

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class StringInputFragment : Fragment("Define value") {
   val initialValue by param("")
   val fieldsetLabel by param(title)
   val label by param("Value: ")

   val valueProperty = SimpleStringProperty(initialValue)
   var value by valueProperty

   private var onCompleteConsumer: ((String) -> Unit)? = null

   fun onComplete(consumer: (String) -> Unit) {
      this.onCompleteConsumer = consumer
   }

   override val root = form {
      fieldset(fieldsetLabel) {
         field(label) {
            textfield(valueProperty) {
               whenDocked { requestFocus() }
            }
         }
      }

      buttonbar {
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