package com.bartlomiejpluta.base.editor.database.model

import javafx.scene.Node
import tornadofx.observableListOf
import java.sql.Connection

class SQLDatabase : SQLElement {
   override val name = "Database"
   val tables = observableListOf<SQLTable>()

   fun addTable(name: String) {
      tables.add(SQLTable(this, name))
   }

   override fun rename(connection: Connection, newName: String) {
      throw UnsupportedOperationException()
   }

   override fun delete(connection: Connection) {
      throw UnsupportedOperationException()
   }

   override val icon: Node? = null
}