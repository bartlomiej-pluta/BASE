package com.bartlomiejpluta.base.editor.database.viewmodel

import com.bartlomiejpluta.base.editor.database.model.data.Query
import tornadofx.ItemViewModel
import tornadofx.getValue

class QueryVM(query: Query) : ItemViewModel<Query>(query) {
   val nameProperty = bind(Query::nameProperty)
   val name by nameProperty

   val queryProperty = bind(Query::queryProperty)
   val query by queryProperty
}