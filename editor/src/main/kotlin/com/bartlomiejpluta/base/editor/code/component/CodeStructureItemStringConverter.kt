package com.bartlomiejpluta.base.editor.code.component

import javafx.scene.control.TreeCell
import javafx.util.StringConverter
import java.io.File

class CodeStructureItemStringConverter(
   private val cell: TreeCell<File>,
   private val onUpdate: (item: File, name: String) -> File
) : StringConverter<File>() {
   override fun toString(item: File?): String = when (item) {
      is File -> item.name
      else -> ""
   }

   // Disclaimer:
   // Because of the fact that we want to support undo/redo mechanism
   // the actual update must be done from the execute() method of the Command object
   // so that the Command object has access to the actual as well as the new value of layer name.
   // That's why we are running the submission logic in the converter.
   override fun fromString(string: String?) = string?.let { onUpdate(cell.item, it) }
}