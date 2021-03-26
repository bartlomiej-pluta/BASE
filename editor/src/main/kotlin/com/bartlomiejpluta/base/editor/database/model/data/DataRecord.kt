package com.bartlomiejpluta.base.editor.database.model.data

import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import kotlin.collections.Map
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach

class DataRecord(val fields: Map<String, DataField>, operation: Operation = Operation.DO_NOTHING) {
   val operationProperty = operation.toProperty()
   var operation by operationProperty

   init {
      fields.forEach { (_, field) -> field.record = this }
   }
}