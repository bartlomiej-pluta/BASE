package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tornadofx.observableListOf
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
                     results.getString("TYPE"),
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

   override fun <T> run(op: Connection.() -> T): T? {
      return projectContext.project?.database?.connection?.use(op)
   }

   override fun execute(statement: String, name: String): Query? = run {
      val stmt = prepareStatement(statement).apply { execute() }
      val results = stmt.resultSet
      val metadata = stmt.metaData

      if (results != null && metadata != null) {
         val columns = observableListOf<String>()

         for (i in 1..metadata.columnCount) {
            columns += metadata.getColumnLabel(i)
         }

         val data = observableListOf<DataRecord>()
         while (results.next()) {
            val record = mutableMapOf<String, DataField>()

            for (i in 1..metadata.columnCount) {
               record[metadata.getColumnLabel(i)] = DataField(results.getObject(i)?.toString())
            }

            data += DataRecord(record)
         }

         return@run Query(name, statement, columns, data)
      }

      return@run null
   }
}