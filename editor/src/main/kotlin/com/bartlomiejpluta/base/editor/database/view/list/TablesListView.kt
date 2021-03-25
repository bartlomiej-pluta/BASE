package com.bartlomiejpluta.base.editor.database.view.list

import com.bartlomiejpluta.base.editor.database.component.SQLElementCell
import com.bartlomiejpluta.base.editor.database.model.*
import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Query
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
import java.sql.Connection
import java.sql.SQLException

class TablesListView : View() {
   private val mainController: MainController by di()
   private val projectContext: ProjectContext by di()
   private val databaseService: DatabaseService by di()

   private var database: SchemaDatabase? = null

   private var index = 0

   private val treeView = treeview<Schema> {
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
            is SchemaDatabase -> value.tables
            is SchemaTable -> value.columns
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

         for(i in 1..metadata.columnCount) {
            columns += metadata.getColumnLabel(i)
         }

         val data = mutableListOf<DataRecord>()
         while (results.next()) {
            val record = mutableMapOf<String, DataField>()

            for (i in 1..metadata.columnCount) {
               record[metadata.getColumnLabel(i)] = DataField(results.getObject(i).toString())
            }

            data += DataRecord(record)
         }

         mainController.openQuery(Query(name, columns, data))
      }

      refresh()
   }

   private fun renameElement(element: Schema, newName: String): Schema {
      onConnection { element.rename(this, newName) }
      return element
   }

   private fun deleteElement(element: Schema) {
      onConnection(element::delete)
   }
}