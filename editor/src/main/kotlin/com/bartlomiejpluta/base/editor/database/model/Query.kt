package com.bartlomiejpluta.base.editor.database.model

import tornadofx.getValue
import tornadofx.toProperty

class Query(name: String, val columns: List<String>, val data: List<Map<String, String>>) {
   val nameProperty = name.toProperty()
   val name by nameProperty
}