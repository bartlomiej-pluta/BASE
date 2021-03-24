package com.bartlomiejpluta.base.editor.database.model

class Table(val name: String) {
   private val mutableColumns = mutableListOf<Column>()
   val columns: List<Column>
      get() = mutableColumns

   fun addColumn(name: String, type: ColumnType, nullable: Boolean, primary: Boolean) {
      val column = Column(this, name, type, nullable, primary)
      mutableColumns += column
   }
}