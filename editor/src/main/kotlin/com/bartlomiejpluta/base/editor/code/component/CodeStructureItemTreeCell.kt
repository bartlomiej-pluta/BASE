package com.bartlomiejpluta.base.editor.code.component

import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.cell.TextFieldTreeCell
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.action
import java.io.File

class CodeStructureItemTreeCell(renameFile: (file: File, name: String) -> File, deleteFile: (file: File) -> Unit) :
   TextFieldTreeCell<File>() {
   private val fileMenu = ContextMenu()

   init {
      converter = CodeStructureItemStringConverter(this, renameFile)
      MenuItem("Rename").apply {
         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }

         fileMenu.items.add(this)
      }

      MenuItem("Delete").apply {
         action {
            deleteFile(item as File)
         }

         fileMenu.items.add(this)
      }
   }

   override fun updateItem(item: File?, empty: Boolean) {
      super.updateItem(item, empty)

      if (empty || item == null) {
         text = null
         graphic = null
         return
      }

      contextMenu = if (isEditing) null else fileMenu
      text = item.name
      graphic = FontIcon(getFileIcon(item))
   }

   companion object {
      private fun getFileIcon(file: File): String {
         if (file.isDirectory) {
            return "fa-folder"
         }

         return when (file.extension.toLowerCase()) {
            "java" -> "fa-code"
            else -> "fa-file"
         }
      }
   }
}