package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import javafx.scene.control.ContextMenu
import javafx.scene.control.cell.TextFieldTreeCell
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.action
import tornadofx.enableWhen
import tornadofx.item
import tornadofx.toProperty

class PrologFileTreeCell(onCreate: (FileNode) -> Unit, onDelete: (FileNode) -> Unit) :
   TextFieldTreeCell<FileNode>() {
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
      item("New File...") {
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

   private fun renameFile(file: FileNode, name: String) = file.apply {
      file.rename(name)
   }

   override fun updateItem(item: FileNode?, empty: Boolean) {
      super.updateItem(item, empty)

      if (empty || item == null) {
         text = null
         graphic = null
         contextMenu = null
         isRoot.value = true
         return
      }

      text = item.name
      graphic = FontIcon(getFileNodeIcon(item))

      contextMenu = when {
         isEditing -> null
         item !is FileSystemNode -> null
         item.type == FileType.FILE -> fileMenu
         item.type == FileType.DIRECTORY -> directoryMenu
         else -> null
      }

      isRoot.value = (item.parent == null)
   }

   companion object {
      private fun getFileNodeIcon(file: FileNode): String {
         if (file.type == FileType.DIRECTORY) {
            return "fa-folder"
         }

         return when (file.extension.toLowerCase()) {
            "java" -> "fa-code"
            else -> "fa-file"
         }
      }
   }
}