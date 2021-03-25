package com.bartlomiejpluta.base.editor.database.view.list

import com.bartlomiejpluta.base.editor.database.component.SchemaElementCell
import com.bartlomiejpluta.base.editor.database.controller.DatabaseController
import com.bartlomiejpluta.base.editor.database.model.*
import com.bartlomiejpluta.base.editor.database.model.schema.Schema
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaDatabase
import com.bartlomiejpluta.base.editor.database.model.schema.SchemaTable
import com.bartlomiejpluta.base.editor.database.service.DatabaseService
import com.bartlomiejpluta.base.editor.file.model.InMemoryStringFileNode
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.TreeItem
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class TablesListView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()
   private val databaseService: DatabaseService by di()
   private val databaseController: DatabaseController by di()

   private var database: SchemaDatabase? = null

   private var index = 0

   private val treeView = treeview<Schema> {
      isShowRoot = false

      setCellFactory {
         SchemaElementCell(this@TablesListView::renameElement, this@TablesListView::deleteElement)
      }

      setOnMouseClicked { event ->
         val selected = selectionModel?.selectedItem?.value
         if (event.clickCount == 2 && selected is SchemaTable) {
            databaseController
               .execute("SELECT * FROM ${selected.name}", selected.name)
               ?.let(mainController::openQuery)
         }
      }
   }

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let { refresh() }
      }
   }

   override val root = borderpane {
      top = toolbar {
         button("SQL Script", graphic = FontIcon("fa-code")) {
            action {
               val name = "Script ${++index}"
               mainController.openScript(
                  fsNode = InMemoryStringFileNode(name, "sql", ""),
                  execute = { code ->
                     databaseController.execute(code, name)?.let(mainController::openQuery)
                     refresh()
                  },
                  saveable = false
               )
            }
         }
      }

      center = treeView
   }

   private fun refresh() {
      databaseService.database.let {
         treeView.root = TreeItem(it)
         database = it
      }

      treeView.populate {
         when (val value = it.value) {
            is SchemaDatabase -> value.tables
            is SchemaTable -> value.columns
            else -> null
         }
      }

      treeView.root.expandTo(1)
   }

   private fun renameElement(element: Schema, newName: String): Schema {
      databaseController.execute { element.rename(this, newName) }
      return element
   }

   private fun deleteElement(element: Schema) {
      databaseController.execute(element::delete)
   }
}