package com.bartlomiejpluta.base.editor.main.component

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.main.model.StructureCategory
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.cell.TextFieldTreeCell
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.action

class StructureItemTreeCell(renameAsset: (asset: Asset, name: String) -> Asset, deleteAsset: (asset: Asset) -> Unit) :
   TextFieldTreeCell<Any>() {
   private val assetMenu = ContextMenu()

   init {
      converter = StructureItemStringConverter(this, renameAsset)
      MenuItem("Rename").apply {
         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }

         assetMenu.items.add(this)
      }

      MenuItem("Delete").apply {
         action {
            deleteAsset(item as Asset)
         }

         assetMenu.items.add(this)
      }
   }

   override fun updateItem(item: Any?, empty: Boolean) {
      super.updateItem(item, empty)

      if (empty || item == null) {
         text = null
         graphic = null
         return
      }

      contextMenu = when (item) {
         is Asset -> if (isEditing) null else assetMenu
         is StructureCategory -> item.menu
         else -> null
      }

      text = when (item) {
         is StructureCategory -> item.name
         is Asset -> item.name
         else -> null
      }

      graphic = when (item) {
         is StructureCategory -> FontIcon("fa-folder")
         is GameMapAsset -> FontIcon("fa-map")
         is TileSetAsset -> FontIcon("fa-th")
         is ImageAsset -> FontIcon("fa-image")
         else -> null
      }
   }
}