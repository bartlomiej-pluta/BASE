package com.bartlomiejpluta.base.editor.common.parameter.component

import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class ParameterValueEditingCell : TableCell<Parameter<*>, Any>() {
   private val parameter: Parameter<*>?
      get() = tableView.items.getOrNull(index)

   private val commitKeyHandler = EventHandler<KeyEvent> {
      if (it.code == KeyCode.ENTER) {
         commitEdit(null)
         it.consume()
      }
   }

   override fun updateItem(item: Any?, empty: Boolean) {
      super.updateItem(item, empty)

      when {
         empty || item == null -> {
            text = null
            graphic = null
         }

         isEditing -> {
            text = null
            graphic = parameter?.editor
         }

         else -> {
            text = parameter?.valueString
            graphic = null
         }
      }
   }

   override fun startEdit() {
      if (index < 0 || index >= tableView.items.size) {
         return
      }

      if (parameter?.editable?.let { !it } == true) {
         return
      }

      super.startEdit()
      text = null
      graphic = parameter?.editor
      parameter?.editor?.addEventHandler(KeyEvent.KEY_PRESSED, commitKeyHandler)
   }

   override fun commitEdit(newValue: Any?) {
      parameter?.commit()
      super.commitEdit(parameter?.value)
      parameter?.editor?.removeEventFilter(KeyEvent.KEY_PRESSED, commitKeyHandler)
   }

   override fun cancelEdit() {
      super.cancelEdit()
      text = parameter?.valueString
      graphic = null
      parameter?.editor?.removeEventFilter(KeyEvent.KEY_PRESSED, commitKeyHandler)
   }
}