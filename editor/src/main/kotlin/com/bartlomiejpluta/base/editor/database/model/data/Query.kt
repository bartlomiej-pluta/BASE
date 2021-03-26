package com.bartlomiejpluta.base.editor.database.model.data

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.getValue
import tornadofx.toProperty

class Query(
   name: String,
   query: String,
   columns: ObservableList<String>,
   data: ObservableList<DataRecord>,
   table: String? = null
) {
   val tableProperty = SimpleStringProperty(table)
   val table by tableProperty

   val nameProperty = name.toProperty()
   val name by nameProperty

   val queryProperty = query.toProperty()
   val query by queryProperty

   val columnsProperty = SimpleListProperty(columns)
   val columns by columnsProperty

   val dataProperty = SimpleListProperty(data)
   val data by dataProperty

   fun addEmptyRecord() {
      val fields = columns.map { it to DataField(null) }.toMap()
      data += DataRecord(fields, Operation.INSERT)
   }
}