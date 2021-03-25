package com.bartlomiejpluta.base.editor.database.view.list

import com.bartlomiejpluta.base.editor.database.component.SQLElementCell
import com.bartlomiejpluta.base.editor.database.model.Query
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
import java.sql.SQLException

class TablesListView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()
   private val databaseService: DatabaseService by di()

   private var database: SQLDatabase? = null

   private var index = 0

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
               val name = "Script ${++index}"
               mainController.openScript(
                  fsNode = InMemoryStringFileNode(name, "sql", ""),
                  execute = { code -> onConnection { executeScript(name, code, this) } },
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

   private fun onConnection(op: Connection.() -> Unit) {
      databaseService.run {
         try {
            op(this)
         } catch (e: SQLException) {
            error("SQL Error ${e.sqlState}", e.joinToString("\n") { e.message ?: "" }, title = "SQL Error")
         }
      }
   }

   private fun executeScript(name: String, sql: String, conn: Connection) {
      val stmt = conn.prepareStatement(sql).apply { execute() }
      val results = stmt.resultSet
      val metadata = stmt.metaData

      if (results != null && metadata != null) {
         val columns = mutableListOf<String>()

         for (i in 1..metadata.columnCount) {
            columns += metadata.getColumnLabel(i)
         }

         val data = mutableListOf<Map<String, String>>()
         while (results.next()) {
            val record = mutableMapOf<String, String>()

            for (i in 1..metadata.columnCount) {
               record[metadata.getColumnLabel(i)] = results.getObject(i).toString()
            }

            data += record
         }

         mainController.openQuery(Query(name, columns, data))
      }

      refresh()
   }

   private fun renameElement(element: SQLElement, newName: String): SQLElement {
      onConnection { element.rename(this, newName) }
      return element
   }

   private fun deleteElement(element: SQLElement) {
      onConnection(element::delete)
   }
}