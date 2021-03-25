package com.bartlomiejpluta.base.editor.database.model.schema

import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.observableListOf
import java.sql.Connection

class SchemaTable(val database: SchemaDatabase, name: String) : Schema {
   override var name: String = name
      private set(value) {
         field = value.toUpperCase()
      }

   val columns = observableListOf<SchemaColumn>()

   fun addColumn(name: String, type: String, nullable: Boolean, primary: Boolean) {
      val column = SchemaColumn(this, name, type, nullable, primary)
      columns += column
   }

   override fun rename(connection: Connection, newName: String) {
      connection.prepareStatement("ALTER TABLE $name RENAME TO $newName").execute()
      name = newName
   }

   override fun delete(connection: Connection) {
      connection.prepareStatement("DROP TABLE $name;").execute()
      database.tables.remove(this)
   }

   override val icon = FontIcon("fa-table")
}