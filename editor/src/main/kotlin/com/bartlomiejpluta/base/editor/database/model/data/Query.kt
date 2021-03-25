package com.bartlomiejpluta.base.editor.database.model.data

import tornadofx.getValue
import tornadofx.toProperty

class Query(name: String, query: String, val columns: List<String>, val data: List<DataRecord>) {
   val nameProperty = name.toProperty()
   val name by nameProperty

   val queryProperty = query.toProperty()
   val query by queryProperty
}