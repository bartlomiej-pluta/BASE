package com.bartlomiejpluta.base.editor.database.view.query

import com.bartlomiejpluta.base.editor.database.component.QueryFieldCell
import com.bartlomiejpluta.base.editor.database.controller.DatabaseController
import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import javafx.scene.control.TableColumn
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class QueryResultView : View() {
   private val databaseController: DatabaseController by di()
   private val queryVM = find<QueryVM>()

   private val table = tableview<DataRecord>()

   init {
      queryVM.itemProperty.addListener { _, _, _ -> refreshData() }

      refreshData()
   }

   private fun refreshData() {
      table.columns.clear()
      table.items.clear()
      queryVM.item?.let { query ->
         table.items.addAll(query.data)
         query.columns.map { column ->
            TableColumn<DataRecord, DataField>(column).apply {
               setCellValueFactory {
                  it.value.fields[column].toProperty()
               }

               setCellFactory { QueryFieldCell() }
            }
         }.forEach { table.addColumnInternal(it) }
      }
   }

   override val root = borderpane {
      top = toolbar {
         button(graphic = FontIcon("fa-refresh")) {
            action {
               databaseController.execute(queryVM.query, queryVM.name)?.let { queryVM.item = it }
            }
         }
      }

      center = table
   }
}