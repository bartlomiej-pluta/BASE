package com.bartlomiejpluta.base.editor.database.model

class Column(val table: Table, val name: String, val type: ColumnType, val nullable: Boolean, val primary: Boolean)