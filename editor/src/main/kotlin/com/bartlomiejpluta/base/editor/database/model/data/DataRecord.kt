package com.bartlomiejpluta.base.editor.database.model.data

import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import tornadofx.getValue
import tornadofx.setValue
import tornadofx.toProperty
import java.sql.Connection
import kotlin.collections.component1
import kotlin.collections.component2

class DataRecord(
   val fields: Map<String, DataField>,
   operation: Operation = Operation.DO_NOTHING,
   private val schema: SchemaTable?
) {
   val operationProperty = operation.toProperty()
   var operation by operationProperty

   init {
      fields.forEach { (_, field) -> field.record = this }
   }

   fun prepareStatement(connection: Connection) = schema?.let { schema ->
      when (operation) {
         Operation.INSERT -> {
            val parametersClause = fields.map { (column, _) -> "`$column` = ?" }.joinToString(", ")
            val sql = "INSERT INTO `${schema.name}` SET $parametersClause"
            val statement = connection.prepareStatement(sql)
            fields.values.forEachIndexed { index, field -> statement.setObject(index + 1, field.value) }

            statement
         }

         Operation.UPDATE -> {
            val fieldsNumber = fields.size
            val pk = schema.columns.filtered { it.primary }
            val filterClause = pk.joinToString(" AND ") { "`${it.name}` = ?" }
            val parametersClause = fields.map { (column, _) -> "`$column` = ?" }.joinToString(", ")
            val sql = "UPDATE `${schema.name}` SET $parametersClause WHERE $filterClause"
            val statement = connection.prepareStatement(sql)
            fields.values.forEachIndexed { index, field -> statement.setObject(index + 1, field.value) }
            pk.forEachIndexed { index, column ->
               statement.setObject(
                  index + 1 + fieldsNumber,
                  fields[column.name]!!.value
               )
            }

            statement
         }

         Operation.DELETE -> {
            val pk = schema.columns.filtered { it.primary }
            val filterClause = pk.joinToString(" AND ") { "`${it.name}` = ?" }
            val sql = "DELETE FROM `${schema.name}` WHERE $filterClause"
            val statement = connection.prepareStatement(sql)
            pk.forEachIndexed { index, column -> statement.setObject(index + 1, fields[column.name]!!.value) }

            statement
         }

         else -> null
      }
   }
}