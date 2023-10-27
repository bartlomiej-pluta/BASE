package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Operation
import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.h2.tools.Script
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

   override fun execute(statement: String, name: String, schema: SchemaTable?): Query? = run {
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
               val field = DataField(results.getObject(i)?.toString())
               record[metadata.getColumnLabel(i)] = field
            }

            data += DataRecord(record, Operation.DO_NOTHING, schema)
         }

         return@run Query(name, statement, columns, data, schema)
      }

      return@run null
   }

   override fun dump() {
      projectContext.project?.databaseFile?.let { file ->
         run {
            Script.process(this, file.absolutePath, "", "")
         }
      }
   }
}