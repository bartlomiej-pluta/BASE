package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import javafx.beans.binding.Bindings
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.TextFieldTreeCell
import javafx.scene.image.Image
import javafx.scene.layout.Priority
import javafx.util.StringConverter
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


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

   private val structureRoot = StructureCategory(
      name = "Project", items = observableListOf(
         structureMaps,
         structureTileSets,
         structureImages
      )
   )

   private var projectStructure: TreeView<Any> by singleAssign()

   private val selectedItem = SimpleObjectProperty<Any>()

   private val graphicAssetPreview = createObjectBinding({
      when (val item = selectedItem.value) {
         is GraphicAsset -> item.graphicFile.inputStream().use { Image(it) }
         else -> null
      }
   }, selectedItem).apply { addListener { _, _, v -> println(v) } }

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            structureRoot.nameProperty.bind(it.nameProperty)
            Bindings.bindContent(structureMaps.items, it.maps)
            Bindings.bindContent(structureTileSets.items, it.tileSets)
            Bindings.bindContent(structureImages.items, it.images)
            projectStructure.root.expandAll()
            projectStructure.refresh()
         }
      }
   }

   override val root = vbox {
      projectStructure = treeview {
         vgrow = Priority.ALWAYS

         root = TreeItem(structureRoot)

         populate {
            when (val value = it.value) {
               is StructureCategory -> value.items
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
               }
            }

            event.consume()
         }

         bindSelected(selectedItem)
      }

      scrollpane {
         vgrow = Priority.SOMETIMES
         prefWidth = 200.0
         prefHeight = 200.0
         removeWhen(graphicAssetPreview.isNull)
         imageview(graphicAssetPreview)
      }
   }

   private fun renameAsset(asset: Asset, name: String) = asset.apply {
      this.name = name
      projectContext.save()
   }

   private fun deleteAsset(asset: Asset) {
      projectContext.deleteAsset(asset)
      projectContext.save()
   }

   private class StructureCategory(name: String = "", var items: ObservableList<out Any> = observableListOf()) {
      val nameProperty = SimpleStringProperty(name)
      val name by nameProperty
      val menu = ContextMenu()

      fun menuitem(text: String, graphic: Node? = null, action: () -> Unit) {
         MenuItem(text, graphic).apply {
            action { action() }
            menu.items.add(this)
         }
      }
   }

   private class StructureItemConverter(
      private val cell: TreeCell<Any>,
      private val onUpdate: (item: Asset, name: String) -> Asset
   ) : StringConverter<Any>() {
      override fun toString(item: Any?): String = when (item) {
         is StructureCategory -> item.name
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
         is StructureCategory -> item.name
         else -> ""
      }
   }

   private class StructureItemTreeCell(
      renameAsset: (asset: Asset, name: String) -> Asset,
      deleteAsset: (asset: Asset) -> Unit
   ) : TextFieldTreeCell<Any>() {
      private val assetMenu = ContextMenu()

      init {
         converter = StructureItemConverter(this, renameAsset)
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
}
