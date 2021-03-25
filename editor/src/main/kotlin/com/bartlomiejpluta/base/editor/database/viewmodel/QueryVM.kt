package com.bartlomiejpluta.base.editor.database.viewmodel

import com.bartlomiejpluta.base.editor.database.model.Query
import tornadofx.ItemViewModel
import tornadofx.getValue

class QueryVM(query: Query) : ItemViewModel<Query>(query) {
   val nameProperty = bind(Query::nameProperty)
   val name by nameProperty
}