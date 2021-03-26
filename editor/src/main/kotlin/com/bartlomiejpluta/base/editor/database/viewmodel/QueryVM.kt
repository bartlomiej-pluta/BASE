package com.bartlomiejpluta.base.editor.database.viewmodel

import com.bartlomiejpluta.base.editor.database.model.data.Query
import tornadofx.ItemViewModel
import tornadofx.getValue

class QueryVM(query: Query) : ItemViewModel<Query>(query) {
   val schemaProperty = bind(Query::schemaProperty)
   val schema by schemaProperty

   val nameProperty = bind(Query::nameProperty)
   val name by nameProperty

   val queryProperty = bind(Query::queryProperty)
   val query by queryProperty

   val columnsProperty = bind(Query::columnsProperty)
   val columns by columnsProperty

   val dataProperty = bind(Query::dataProperty)
   val data by dataProperty
}