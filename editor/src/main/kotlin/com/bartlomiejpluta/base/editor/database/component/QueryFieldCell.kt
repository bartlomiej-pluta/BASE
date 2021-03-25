package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import javafx.scene.control.TableCell

class QueryFieldCell : TableCell<DataRecord, DataField>() {

   override fun updateItem(item: DataField?, empty: Boolean) {
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