package com.bartlomiejpluta.base.editor.asset.component

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.model.AssetCategory
import javafx.scene.control.TreeCell
import javafx.util.StringConverter

class AssetStringConverter(
   private val cell: TreeCell<Any>,
   private val onUpdate: (item: Asset, name: String) -> Asset
) : StringConverter<Any>() {
   override fun toString(item: Any?): String = when (item) {
      is AssetCategory -> item.name
      is Asset -> item.name
      else -> ""
   }

   // Disclaimer:
   // Because of the fact that we want to support undo/redo mechanism
   // the actual update must be done from the execute() method of the Command object
   // so that the Command object has access to the actual as well as the new value of layer name.
   // That's why we are running the submission logic in the converter.
   override fun fromString(string: String?): Any = when (val item = cell.item) {
      is Asset -> string?.let { onUpdate(item, it) } ?: ""
      is AssetCategory -> item.name
      else -> ""
   }
}