package com.bartlomiejpluta.base.editor.database.model.data

import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import tornadofx.getValue
import tornadofx.toProperty

class Query(
   name: String,
   query: String,
   columns: ObservableList<String>,
   data: ObservableList<DataRecord>,
   schema: SchemaTable? = null
) {
   val schemaProperty = SimpleObjectProperty<SchemaTable>(schema)
   val schema by schemaProperty

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
      data += DataRecord(fields, Operation.INSERT, schema)
   }
}