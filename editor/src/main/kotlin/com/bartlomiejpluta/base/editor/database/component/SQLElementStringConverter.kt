package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.SQLElement
import javafx.scene.control.TreeCell
import javafx.util.StringConverter

class SQLElementStringConverter(
   private val cell: TreeCell<SQLElement>,
   private val rename: (item: SQLElement, newName: String) -> SQLElement
) : StringConverter<SQLElement>() {
   override fun toString(item: SQLElement?): String = item?.name ?: ""

   // Disclaimer:
   // This is actually the only place where we have an access to both actual element
   // with untouched name and new element name so that's why we should call `rename()` function right here.
   override fun fromString(string: String?) = string?.let { rename(cell.item, it) }
}