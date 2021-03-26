package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import java.sql.Connection

interface DatabaseService {
   val database: SchemaDatabase

   fun <T> run(op: Connection.() -> T): T?

   fun execute(statement: String, name: String, table: String? = null): Query?
}