package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.schema.Schema
import javafx.scene.control.TreeCell
import javafx.util.StringConverter

class SchemaElementStringConverter(
   private val cell: TreeCell<Schema>,
   private val rename: (item: Schema, newName: String) -> Schema
) : StringConverter<Schema>() {
   override fun toString(item: Schema?): String = item?.name ?: ""

   // Disclaimer:
   // This is actually the only place where we have an access to both actual element
   // with untouched name and new element name so that's why we should call `rename()` function right here.
   override fun fromString(string: String?) = string?.let { rename(cell.item, it) }
}