package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.main.component.StructureItemTreeCell
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.main.model.StructureCategory
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*
import java.io.File


class ProjectStructureView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val structureMaps = StructureCategory("Maps").apply {
      menuitem("New Map...") { mainController.createEmptyMap() }
   }

   private val structureTileSets = StructureCategory("Tile Sets").apply {
      menuitem("Import Tile Set...") { mainController.importTileSet() }
   }

   private val structureImages = StructureCategory("Images").apply {
      menuitem("Import Image...") { mainController.importImage() }
   }

   private val structureCode = StructureCategory("Code").apply {
      menuitem("Refresh") { this@ProjectStructureView.treeView.refresh() }
   }

   private val structureRoot = StructureCategory(
      name = "Project", items = observableListOf(
         structureMaps,
         structureTileSets,
         structureImages,
         structureCode
      )
   )

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            structureRoot.nameProperty.bind(it.nameProperty)
            Bindings.bindContent(structureMaps.items, it.maps)
            Bindings.bindContent(structureTileSets.items, it.tileSets)
            Bindings.bindContent(structureImages.items, it.images)

            structureCode.items.clear()
            structureCode.items.add(it.codeDirectory)

            root.root.expandAll()
            root.refresh()
         }
      }
   }

   private val treeView: TreeView<Any> = treeview<Any> {
      root = TreeItem(structureRoot)

      populate {
         when (val value = it.value) {
            is StructureCategory -> value.items
            is File -> value.listFiles()?.toList() ?: observableListOf()
            else -> null
         }
      }

      setCellFactory {
         StructureItemTreeCell(this@ProjectStructureView::renameAsset, this@ProjectStructureView::deleteAsset)
      }

      setOnMouseClicked { event ->
         if (event.clickCount == 2) {
            when (val item = selectionModel?.selectedItem?.value) {
               is GameMapAsset -> mainController.openMap(item.uid)
               is File -> item.takeIf { it.isFile }?.let { mainController.openScript(item) }
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
