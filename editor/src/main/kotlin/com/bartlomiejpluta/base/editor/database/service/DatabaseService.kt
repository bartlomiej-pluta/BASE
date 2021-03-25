package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import java.sql.Connection

interface DatabaseService {
   val database: SchemaDatabase
   fun run(op: Connection.() -> Unit)
}