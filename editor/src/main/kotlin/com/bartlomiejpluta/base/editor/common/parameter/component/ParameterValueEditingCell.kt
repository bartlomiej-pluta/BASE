package com.bartlomiejpluta.base.editor.common.parameter.component

import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import javafx.scene.control.TableCell

class ParameterValueEditingCell : TableCell<Parameter<*>, Any>() {

   override fun updateItem(item: Any?, empty: Boolean) {
      super.updateItem(item, empty)

      when {
         empty || item == null -> {
            text = null
            graphic = null
         }

         isEditing -> {
            text = null
            graphic = tableView.items[index].editor
         }

         else -> {
            text = tableView.items[index].valueString
            graphic = null
         }
      }
   }

   override fun startEdit() {
      if (index < 0 || index >= tableView.items.size) {
         return
      }

      if (!tableView.items[index].editable) {
         return
      }

      super.startEdit()
      text = null
      graphic = tableView.items[index].editor
   }

   override fun cancelEdit() {
      super.cancelEdit()
      text = tableView.items[index]?.valueString
      graphic = null
   }
}