package com.bartlomiejpluta.base.editor.database.controller

import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import com.bartlomiejpluta.base.editor.database.service.DatabaseService
import org.springframework.stereotype.Component
import tornadofx.Controller
import tornadofx.error
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

@Component
class DatabaseController : Controller() {
   private val databaseService: DatabaseService by di()

   fun execute(statement: String, name: String, schema: SchemaTable? = null): Query? = try {
      databaseService.execute(statement, name, schema)
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

   fun submitBatch(records: List<DataRecord>) = execute {
      autoCommit = false

      records
         .mapNotNull { it.prepareStatement(this) }
         .forEach(PreparedStatement::execute)

      commit()
   }

   private fun sqlErrorAlert(e: SQLException) =
      error("SQL Error ${e.sqlState}", e.joinToString("\n") { e.message ?: "" }, title = "SQL Error")
}