package com.bartlomiejpluta.base.editor.main.view

import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem
import javafx.scene.input.MouseEvent
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*


class ProjectStructureView : View() {
   private val projectContext: ProjectContext by di()
   private val mainController: MainController by di()

   private val structureMaps = StructureCategory("Maps")

   private val structureRoot = StructureCategory(name = "Project", items = observableListOf(structureMaps))

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let {
            structureRoot.nameProperty.bind(it.nameProperty)
            Bindings.bindContent(structureMaps.items, project.maps)
            root.root.expandAll()
            root.refresh()
         }
      }
   }

   override val root = treeview<Any> {
      root = TreeItem(structureRoot)

      cellFormat {
         graphic = when(it) {
            structureRoot -> FontIcon("fa-cog")
            is StructureCategory -> FontIcon("fa-folder")
            is GameMapAsset -> FontIcon("fa-map")
            else -> null
         }

         text = when (it) {
            is StructureCategory -> it.name
            is GameMapAsset -> it.name
            else -> throw IllegalStateException("Unsupported structure item type")
         }
      }

      populate {
         when (val value = it.value) {
            is StructureCategory -> value.items
            else -> null
         }
      }

      setOnMouseClicked { event ->
         if(event.clickCount == 2) {
            when(val item = selectionModel?.selectedItem?.value) {
               is GameMapAsset -> mainController.openMap(item.uid)
            }
         }
      }
   }

   private class StructureCategory(name: String = "", var items: ObservableList<out Any> = observableListOf()) {
      val nameProperty = SimpleStringProperty(name)
      val name by nameProperty
   }
}