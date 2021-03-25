package com.bartlomiejpluta.base.editor.database.model

import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.observableListOf
import java.sql.Connection

class SQLTable(val database: SQLDatabase, name: String) : SQLElement {
   override var name: String = name
      private set(value) {
         field = value.toUpperCase()
      }

   val columns = observableListOf<SQLColumn>()

   fun addColumn(name: String, type: ColumnType, nullable: Boolean, primary: Boolean) {
      val column = SQLColumn(this, name, type, nullable, primary)
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