package com.bartlomiejpluta.base.editor.asset.view.list

import com.bartlomiejpluta.base.editor.asset.component.AssetTreeCell
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.model.AssetCategory
import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings.bindContent
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*

class AssetsListView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val maps = AssetCategory("Maps").apply {
      menuitem("New Map...") { mainController.createEmptyMap() }
      menuitem("Import Map...") { mainController.importMap() }
   }

   private val tileSets = AssetCategory("Tile Sets").apply {
      menuitem("Import Tile Set...") { mainController.importTileSet() }
   }

   private val autoTiles = AssetCategory("Auto Tiles").apply {
      menuitem("Import Auto Tile...") { mainController.importAutoTile() }
   }

   private val images = AssetCategory("Images").apply {
      menuitem("Import Image...") { mainController.importImage() }
   }

   private val characterSet = AssetCategory("Character Sets").apply {
      menuitem("Import Character Set...") { mainController.importCharacterSet() }
   }

   private val animations = AssetCategory("Animations").apply {
      menuitem("Import Animation...") { mainController.importAnimation() }
   }

   private val iconSets = AssetCategory("Icon Sets").apply {
      menuitem("Import Icon Set...") { mainController.importIconSet() }
   }

   private val fonts = AssetCategory("Fonts").apply {
      menuitem("Import Font...") { mainController.importFont() }
   }

   private val widgets = AssetCategory("Widgets").apply {
      menuitem("New Widget...") { mainController.createEmptyWidget() }
   }

   private val audio = AssetCategory("Audio").apply {
      menuitem("Import Sound...") { mainController.importSound() }
   }

   private val rootItem = AssetCategory(
      name = "Project", items = observableListOf(
         maps,
         tileSets,
         autoTiles,
         images,
         characterSet,
         animations,
         iconSets,
         fonts,
         widgets,
         audio
      )
   )

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            rootItem.nameProperty.bind(it.nameProperty)
            bindContent(maps.items, it.maps)
            bindContent(tileSets.items, it.tileSets)
            bindContent(autoTiles.items, it.autoTiles)
            bindContent(images.items, it.images)
            bindContent(characterSet.items, it.characterSets)
            bindContent(animations.items, it.animations)
            bindContent(iconSets.items, it.iconSets)
            bindContent(fonts.items, it.fonts)
            bindContent(widgets.items, it.widgets)
            bindContent(audio.items, it.sounds)
            root.root.expandAll()
         }
      }
   }

   private val treeView: TreeView<Any> = treeview<Any> {
      root = TreeItem(rootItem)

      isShowRoot = false

      populate {
         when (val value = it.value) {
            is AssetCategory -> value.items
            else -> null
         }
      }

      setCellFactory {
         AssetTreeCell(this@AssetsListView::renameAsset, this@AssetsListView::deleteAsset)
      }

      setOnMouseClicked { event ->
         if (event.clickCount == 2) {
            when (val item = selectionModel?.selectedItem?.value) {
               is GameMapAsset -> mainController.openMap(item.uid)
               is ScriptAssetFileNode -> mainController.openScript(item)
               is GraphicAsset -> mainController.openGraphicAsset(item)
            }
         }

         event.consume()
      }
   }

   override val root = treeView

   private fun renameAsset(asset: Asset, name: String) = asset.apply {
      this.name = name
      projectContext.save()
   }

   private fun deleteAsset(asset: Asset) {
      mainController.closeAsset(asset)
      projectContext.deleteAsset(asset)
      projectContext.save()
   }
}