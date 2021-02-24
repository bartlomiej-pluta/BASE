package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import javafx.scene.control.ContextMenu
import javafx.scene.control.cell.TextFieldTreeCell
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.action
import tornadofx.enableWhen
import tornadofx.item
import tornadofx.toProperty

class ScriptFileTreeCell(onCreate: (FileSystemNode) -> Unit, onDelete: (FileSystemNode) -> Unit) :
   TextFieldTreeCell<FileSystemNode>() {
   private val isRoot = true.toProperty()
   private val isNotRoot = isRoot.not()

   private val fileMenu = ContextMenu().apply {
      item("Rename") {
         enableWhen(isNotRoot)

         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }
      }

      item("Delete") {
         enableWhen(isNotRoot)

         action {
            item.delete()
            onDelete(item)
         }
      }
   }

   private val directoryMenu = ContextMenu().apply {
      item("New Class...") {
         action { onCreate(item) }
      }

      item("Refresh") {
         action { item.refresh() }
      }

      item("Rename") {
         enableWhen(isNotRoot)

         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }
      }

      item("Delete") {
         enableWhen(isNotRoot)

         action {
            item.delete()
            onDelete(item)
         }
      }
   }

   init {
      converter = ScriptFileStringConverter(this, this::renameFile)
   }

   private fun renameFile(file: FileSystemNode, name: String) = file.apply {
      file.rename(name)
   }

   override fun updateItem(item: FileSystemNode?, empty: Boolean) {
      super.updateItem(item, empty)

      if (empty || item == null) {
         text = null
         graphic = null
         contextMenu = null
         isRoot.value = true
         return
      }

      text = item.file.name
      graphic = FontIcon(getFileSystemNodeIcon(item))

      contextMenu = when {
         isEditing -> null
         item.isFile -> fileMenu
         item.isDirectory -> directoryMenu
         else -> null
      }

      isRoot.value = (item.parent == null)
   }

   companion object {
      private fun getFileSystemNodeIcon(file: FileSystemNode): String {
         if (file.isDirectory) {
            return "fa-folder"
         }

         return when (file.file.extension.toLowerCase()) {
            "java" -> "fa-code"
            else -> "fa-file"
         }
      }
   }
}