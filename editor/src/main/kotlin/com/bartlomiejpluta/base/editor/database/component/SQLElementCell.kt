package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.SQLColumn
import com.bartlomiejpluta.base.editor.database.model.SQLElement
import com.bartlomiejpluta.base.editor.database.model.SQLTable
import javafx.scene.control.ContextMenu
import javafx.scene.control.cell.TextFieldTreeCell
import tornadofx.action
import tornadofx.item

class SQLElementCell(
   renameElement: (element: SQLElement, name: String) -> SQLElement,
   deleteElement: (element: SQLElement) -> Unit
) : TextFieldTreeCell<SQLElement>() {
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
      converter = SQLElementStringConverter(this, renameElement)
   }

   override fun updateItem(item: SQLElement?, empty: Boolean) {
      super.updateItem(item, empty)

      if (empty || item == null) {
         text = null
         graphic = null
         return
      }

      text = when (item) {
         is SQLColumn -> "${item.name}${if (item.nullable) "?" else ""}"
         else -> item.name
      }

      graphic = item.icon

      contextMenu = when (item) {
         is SQLTable -> tableMenu
         is SQLColumn -> columnMenu
         else -> null
      }
   }
}