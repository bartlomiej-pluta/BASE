package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.file.model.FileNode
import javafx.scene.control.TreeCell
import javafx.util.StringConverter

class ScriptFileStringConverter(
   private val cell: TreeCell<FileNode>,
   private val onUpdate: (item: FileNode, name: String) -> FileNode
) : StringConverter<FileNode>() {
   override fun toString(item: FileNode?): String = when (item) {
      is FileNode -> item.name
      else -> ""
   }

   // Disclaimer:
   // Because of the fact that we want to support undo/redo mechanism
   // the actual update must be done from the execute() method of the Command object
   // so that the Command object has access to the actual as well as the new value of layer name.
   // That's why we are running the submission logic in the converter.
   override fun fromString(string: String?) = string?.let { onUpdate(cell.item, it) }
}