package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.schema.Schema
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaColumn
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import javafx.scene.control.ContextMenu
import javafx.scene.control.cell.TextFieldTreeCell
import tornadofx.action
import tornadofx.item

class SchemaElementCell(
   renameElement: (element: Schema, name: String) -> Schema,
   deleteElement: (element: Schema) -> Unit
) : TextFieldTreeCell<Schema>() {
   private val tableMenu = ContextMenu().apply {
      item("Rename") {
         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }
      }

      item("Delete") {
         action {
            deleteElement(item)
         }
      }
   }

   private val columnMenu = ContextMenu().apply {
      item("Rename") {
         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }
      }

      item("Delete") {
         action {
            deleteElement(item)
         }
      }
   }

   init {
      converter = SchemaElementStringConverter(this, renameElement)
   }

   override fun updateItem(item: Schema?, empty: Boolean) {
      super.updateItem(item, empty)

      if (empty || item == null) {
         text = null
         graphic = null
         return
      }

      text = when (item) {
         is SchemaColumn -> "${item.name}: ${item.rawType}${if (item.nullable) "?" else ""}"
         else -> item.name
      }

      graphic = item.icon

      contextMenu = when (item) {
         is SchemaTable -> tableMenu
         is SchemaColumn -> columnMenu
         else -> null
      }
   }
}