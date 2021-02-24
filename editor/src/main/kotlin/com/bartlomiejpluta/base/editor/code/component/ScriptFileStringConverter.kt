package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import javafx.scene.control.TreeCell
import javafx.util.StringConverter

class ScriptFileStringConverter(
   private val cell: TreeCell<FileSystemNode>,
   private val onUpdate: (item: FileSystemNode, name: String) -> FileSystemNode
) : StringConverter<FileSystemNode>() {
   override fun toString(item: FileSystemNode?): String = when (item) {
      is FileSystemNode -> item.file.name
      else -> ""
   }

   // Disclaimer:
   // Because of the fact that we want to support undo/redo mechanism
   // the actual update must be done from the execute() method of the Command object
   // so that the Command object has access to the actual as well as the new value of layer name.
   // That's why we are running the submission logic in the converter.
   override fun fromString(string: String?) = string?.let { onUpdate(cell.item, it) }
}