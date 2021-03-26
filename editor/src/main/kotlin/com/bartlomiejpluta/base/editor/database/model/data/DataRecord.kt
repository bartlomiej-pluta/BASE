package com.bartlomiejpluta.base.editor.database.model.data

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import kotlin.collections.component1
import kotlin.collections.component2

class DataRecord(val fields: Map<String, DataField>, operation: Operation = Operation.DO_NOTHING) {
   val operationProperty = operation.toProperty()
   var operation by operationProperty

   init {
      fields.forEach { (_, field) -> field.record = this }
   }

   fun prepareStatement(table: String) = when (operation) {
      Operation.INSERT -> "INSERT INTO `$table` SET $parameters;"
      else -> null
   }

   private val parameters = fields.map { (column, _) -> "`$column` = ?" }.joinToString(", ")
}