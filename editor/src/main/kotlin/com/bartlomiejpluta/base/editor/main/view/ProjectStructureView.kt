package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.cell.TextFieldTreeCell
import javafx.util.StringConverter
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class ProjectStructureView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val structureMaps = StructureCategory("Maps")
   private val structureTileSets = StructureCategory("Tile Sets")
   private val structureImages = StructureCategory("Images")

   private val structureRoot = StructureCategory(
      name = "Project", items = observableListOf(
         structureMaps,
         structureTileSets,
         structureImages
      )
   )

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            structureRoot.nameProperty.bind(it.nameProperty)
            Bindings.bindContent(structureMaps.items, it.maps)
            Bindings.bindContent(structureTileSets.items, it.tileSets)
            Bindings.bindContent(structureImages.items, it.images)
            root.root.expandAll()
            root.refresh()
         }
      }
   }

   override val root = treeview<Any> {
      root = TreeItem(structureRoot)

      populate {
         when (val value = it.value) {
            is StructureCategory -> value.items
            else -> null
         }
      }

      setCellFactory {
         StructureItemTreeCell { item, name ->
            item.apply {
               if (this is Asset) {
                  this.name = name
                  projectContext.save()
               }
            }
         }
      }

      setOnMouseClicked { event ->
         if (event.clickCount == 2) {
            when (val item = selectionModel?.selectedItem?.value) {
               is GameMapAsset -> mainController.openMap(item.uid)
            }
         }

         event.consume()
      }
   }

   private class StructureCategory(name: String = "", var items: ObservableList<out Any> = observableListOf()) {
      val nameProperty = SimpleStringProperty(name)
      val name by nameProperty
   }

   private class StructureItemConverter(
      private val cell: TreeCell<Any>,
      private val onUpdate: (item: Any, name: String) -> Any
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
      override fun fromString(string: String?): Any = string?.let { onUpdate(cell.item, it) } ?: ""
   }

   private class StructureItemTreeCell(onUpdate: (item: Any, name: String) -> Any) : TextFieldTreeCell<Any>() {
      private val assetMenu = ContextMenu()

      init {
         converter = StructureItemConverter(this, onUpdate)
         MenuItem("Rename").apply {
            action {
               treeView.isEditable = true
               startEdit()
               treeView.isEditable = false
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

         if (!isEditing && item is Asset) {
            contextMenu = assetMenu
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
