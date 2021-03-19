package com.bartlomiejpluta.base.editor.asset.component

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAsset
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.model.AssetCategory
import com.bartlomiejpluta.base.editor.entityset.asset.EntitySet
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import javafx.scene.control.ContextMenu
import javafx.scene.control.cell.TextFieldTreeCell
import javafx.scene.input.Clipboard
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.action
import tornadofx.item
import tornadofx.putString

class AssetTreeCell(renameAsset: (asset: Asset, name: String) -> Asset, deleteAsset: (asset: Asset) -> Unit) :
   TextFieldTreeCell<Any>() {

   private val assetMenu = ContextMenu().apply {
      item("Copy UID") {
         action {
            Clipboard.getSystemClipboard().putString((item as Asset).uid)
         }
      }

      item("Rename") {
         action {
            treeView.isEditable = true
            startEdit()
            treeView.isEditable = false
         }
      }

      item("Delete") {
         action {
            deleteAsset(item as Asset)
         }
      }
   }

   init {
      converter = AssetStringConverter(this, renameAsset)
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
         is AssetCategory -> item.menu
         else -> null
      }

      text = when (item) {
         is AssetCategory -> item.name
         is Asset -> item.name
         else -> null
      }

      graphic = when (item) {
         is AssetCategory -> FontIcon("fa-folder")
         is GameMapAsset -> FontIcon("fa-map")
         is TileSetAsset -> FontIcon("fa-th")
         is ImageAsset -> FontIcon("fa-image")
         is EntitySet -> FontIcon("fa-male")
         is AnimationAsset -> FontIcon("fa-film")
         is FontAsset -> FontIcon("fa-font")
         is WidgetAsset -> FontIcon("fa-tachometer")
         else -> null
      }
   }
}