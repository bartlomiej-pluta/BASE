package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Operation
import javafx.scene.control.TableCell
import javafx.util.StringConverter

class DataFieldStringConverter : StringConverter<DataField>() {
   lateinit var cell: TableCell<DataRecord, DataField>

   override fun toString(item: DataField?): String = cell.item?.value ?: ""

   override fun fromString(string: String?): DataField = cell.item.apply {
      if (record.operation == Operation.DO_NOTHING) {
         record.operation = Operation.UPDATE
      }

      value = string
   }
}