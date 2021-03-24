package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.ColumnType
import com.bartlomiejpluta.base.editor.database.model.Table
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Connection

@Service
class H2DatabaseService : DatabaseService {

   @Autowired
   private lateinit var projectContext: ProjectContext

   override val tables: List<Table>
      get() {
         val tables = connection {
            val tables = mutableListOf<Table>()
            val results = prepareStatement("SHOW TABLES").executeQuery()
            while (results.next()) {
               if (results.getString("TABLE_SCHEMA") == "PUBLIC") {
                  tables.add(Table(results.getString("TABLE_NAME")))
               }
            }

            tables
         } ?: emptyList()

         tables.forEach { table ->
            connection {
               val results = prepareStatement("SHOW COLUMNS FROM ${table.name}").executeQuery()
               while (results.next()) {
                  table.addColumn(
                     results.getString("FIELD"),
                     parseType(results.getString("TYPE")),
                     results.getBoolean("NULL"),
                     results.getString("KEY") == "PRI"
                  )
               }
            }
         }

         return tables
      }

   private inline fun <reified T> connection(op: Connection.() -> T) =
      projectContext.project?.database?.connection?.use(op)

   private fun parseType(type: String) = ColumnType.valueOf(type.replace(" ", "_").substringBefore("("))
}