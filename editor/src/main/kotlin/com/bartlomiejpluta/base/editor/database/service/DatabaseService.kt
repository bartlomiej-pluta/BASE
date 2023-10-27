package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.data.Query
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import java.sql.Connection

interface DatabaseService {
   val database: SchemaDatabase

   fun <T> run(op: Connection.() -> T): T?

   fun execute(statement: String, name: String, schema: SchemaTable? = null): Query?

   fun dump()
}