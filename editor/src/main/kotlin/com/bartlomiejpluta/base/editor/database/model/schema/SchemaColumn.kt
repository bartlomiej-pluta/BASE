package com.bartlomiejpluta.base.editor.database.model.schema

import org.kordamp.ikonli.javafx.FontIcon
import java.sql.Connection

class SchemaColumn(
   val table: SchemaTable,
   name: String,
   val rawType: String,
   val nullable: Boolean,
   val primary: Boolean
) : Schema {
   val type = ColumnType.valueOf(rawType.replace(" ", "_").substringBefore("("))

   override var name: String = name
      private set(value) {
         field = value.toUpperCase()
      }

   override fun rename(connection: Connection, newName: String) {
      connection.prepareStatement("ALTER TABLE ${table.name} ALTER COLUMN $name RENAME TO $newName").execute()
      name = newName
   }

   override fun delete(connection: Connection) {
      connection.prepareStatement("ALTER TABLE ${table.name} DROP COLUMN $name").execute()
      table.columns.remove(this)
   }

   override val icon = if (primary) FontIcon("fa-key") else when (type) {
      ColumnType.INTEGER -> FontIcon("fa-hashtag")
      ColumnType.BOOLEAN -> FontIcon("fa-toggle-on")
      ColumnType.TINYINT -> FontIcon("fa-hashtag")
      ColumnType.SMALLINT -> FontIcon("fa-hashtag")
      ColumnType.BIGINT -> FontIcon("fa-hashtag")
      ColumnType.DECIMAL -> FontIcon("fa-usd")
      ColumnType.DOUBLE -> FontIcon("fa-hashtag")
      ColumnType.REAL -> FontIcon("fa-hashtag")
      ColumnType.TIME -> FontIcon("fa-clock")
      ColumnType.TIME_WITH_TIME_ZONE -> FontIcon("fa-clock")
      ColumnType.DATE -> FontIcon("fa-calendar")
      ColumnType.TIMESTAMP -> FontIcon("fa-calendar")
      ColumnType.TIMESTAMP_WITH_TIME_ZONE -> FontIcon("fa-calendar")
      ColumnType.VARCHAR -> FontIcon("fa-font")
      ColumnType.VARCHAR_IGNORECASE -> FontIcon("fa-font")
      ColumnType.CHAR -> FontIcon("fa-header")
      ColumnType.ARRAY -> FontIcon("fa-list-ol")
      ColumnType.GEOMETRY -> FontIcon("fa-globe")
      ColumnType.JSON -> FontIcon("fa-code")
      else -> FontIcon("fa-cube")
   }
}