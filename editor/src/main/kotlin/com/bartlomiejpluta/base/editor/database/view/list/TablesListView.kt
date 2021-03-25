package com.bartlomiejpluta.base.editor.database.view.list

import com.bartlomiejpluta.base.editor.database.component.SQLElementCell
import com.bartlomiejpluta.base.editor.database.model.SQLDatabase
import com.bartlomiejpluta.base.editor.database.model.SQLElement
import com.bartlomiejpluta.base.editor.database.model.SQLTable
import com.bartlomiejpluta.base.editor.database.service.DatabaseService
import com.bartlomiejpluta.base.editor.file.model.InMemoryStringFileNode
import com.bartlomiejpluta.base.editor.main.controller.MainController
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.scene.control.TreeItem
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*
import java.sql.Connection

class TablesListView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()
   private val databaseService: DatabaseService by di()

   private var database: SQLDatabase? = null

   private val treeView = treeview<SQLElement> {
      isShowRoot = false

      setCellFactory {
         SQLElementCell(this@TablesListView::renameElement, this@TablesListView::deleteElement)
      }
   }

   init {
      projectContext.projectProperty.addListener { _, _, project ->
         project?.let { refresh() }
      }
   }

   override val root = vbox {
      toolbar {
         button("SQL Script", graphic = FontIcon("fa-code")) {
            action {
               mainController.openScript(
                  fsNode = InMemoryStringFileNode("Script", "sql", "SELECT * FROM myTable;"),
                  execute = { code -> databaseService.run { executeScript(code, this) } },
                  saveable = false
               )
            }
         }
      }

      this += treeView
   }

   private fun refresh() {
      databaseService.database.let {
         treeView.root = TreeItem(it)
         database = it
      }

      treeView.populate {
         when (val value = it.value) {
            is SQLDatabase -> value.tables
            is SQLTable -> value.columns
            else -> null
         }
      }

      treeView.root.expandTo(1)
   }

   private fun executeScript(sql: String, conn: Connection) {
      val stmt = conn.prepareStatement(sql)
      stmt.execute()
      val rs = stmt.resultSet
      val meta = stmt.metaData

      if (meta != null) {
         for (i in 1..meta.columnCount) {
            print(meta.getColumnLabel(i))
            print(" ")
         }

         println()
      }

      if (rs != null) {
         while (rs.next()) {
            for (i in 1..meta.columnCount) {
               print(rs.getObject(i))
               print(" ")
            }

            println()
         }
      }

      refresh()
   }

   private fun renameElement(element: SQLElement, newName: String): SQLElement {
      databaseService.run { element.rename(this, newName) }
      return element
   }

   private fun deleteElement(element: SQLElement) {
      databaseService.run(element::delete)
   }
}