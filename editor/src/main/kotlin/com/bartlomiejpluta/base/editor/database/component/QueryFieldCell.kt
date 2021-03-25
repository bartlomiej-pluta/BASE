package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.Field
import com.bartlomiejpluta.base.editor.database.model.Row
import javafx.scene.control.TableCell

class QueryFieldCell : TableCell<Row, Field>() {

   override fun updateItem(item: Field?, empty: Boolean) {
      super.updateItem(item, empty)

      when {
         empty || item == null -> {
            text = null
            graphic = null
         }

         else -> {
            text = item.value
            graphic = null
         }
      }
   }
}