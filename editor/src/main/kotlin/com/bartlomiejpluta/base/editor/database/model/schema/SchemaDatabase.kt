package com.bartlomiejpluta.base.editor.database.model.schema

import javafx.scene.Node
import tornadofx.observableListOf
import java.sql.Connection

class SchemaDatabase : Schema {
   override val name = "Database"
   val tables = observableListOf<SchemaTable>()

   fun addTable(name: String) {
      tables.add(SchemaTable(this, name))
   }

   override fun rename(connection: Connection, newName: String) {
      throw UnsupportedOperationException()
   }

   override fun delete(connection: Connection) {
      throw UnsupportedOperationException()
   }

   override val icon: Node? = null
}