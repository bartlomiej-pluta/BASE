package com.bartlomiejpluta.base.editor.database.service

import com.bartlomiejpluta.base.editor.database.model.SQLDatabase
import java.sql.Connection

interface DatabaseService {
   val database: SQLDatabase
   fun run(op: Connection.() -> Unit)
}