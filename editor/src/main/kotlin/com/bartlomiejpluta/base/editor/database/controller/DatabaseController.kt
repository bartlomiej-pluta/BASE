package com.bartlomiejpluta.base.editor.database.controller

import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.service.DatabaseService
import org.springframework.stereotype.Component
import tornadofx.Controller
import tornadofx.error
import java.sql.Connection
import java.sql.SQLException

@Component
class DatabaseController : Controller() {
   private val databaseService: DatabaseService by di()

   fun execute(statement: String, name: String, table: String? = null): Query? = try {
      databaseService.execute(statement, name, table)
   } catch (e: SQLException) {
      sqlErrorAlert(e)
      null
   }

   fun execute(op: Connection.() -> Unit): Boolean = databaseService.run {
      try {
         op(this)
         true
      } catch (e: SQLException) {
         sqlErrorAlert(e)
         false
      }
   } ?: false

   fun submitBatch(table: String, records: List<DataRecord>) = execute {
      autoCommit = false
      records.forEach {
         it.prepareStatement(table)?.let { sql ->
            val statement = prepareStatement(sql)
            it.fields.values.forEachIndexed { index, field -> statement.setObject(index + 1, field.value) }
            statement.execute()
         }
      }

      commit()
   }

   private fun sqlErrorAlert(e: SQLException) =
      error("SQL Error ${e.sqlState}", e.joinToString("\n") { e.message ?: "" }, title = "SQL Error")
}