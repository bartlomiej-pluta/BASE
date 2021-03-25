package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.schema.ColumnType
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Connection

@Service
class H2DatabaseService : DatabaseService {
   override val database: SchemaDatabase
      get() {
         val db = SchemaDatabase()

         run {
            val results = prepareStatement("SHOW TABLES").executeQuery()
            while (results.next()) {
               if (results.getString("TABLE_SCHEMA") == "PUBLIC") {
                  db.addTable(results.getString("TABLE_NAME"))
               }
            }
         }

         db.tables.forEach { table ->
            run {
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

         return db
      }

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun run(op: Connection.() -> Unit) {
      projectContext.project?.database?.connection?.use(op)
   }

   private fun parseType(type: String) = ColumnType.valueOf(type.replace(" ", "_").substringBefore("("))
}