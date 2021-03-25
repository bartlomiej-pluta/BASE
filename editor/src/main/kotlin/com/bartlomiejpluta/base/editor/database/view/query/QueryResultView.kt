package com.bartlomiejpluta.base.editor.database.view.query

import com.bartlomiejpluta.base.editor.database.component.QueryFieldCell
import com.bartlomiejpluta.base.editor.database.model.Field
import com.bartlomiejpluta.base.editor.database.model.Row
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import javafx.scene.control.TableColumn
import tornadofx.*

class QueryResultView : View() {
   private val queryVM = find<QueryVM>()

   private val table = tableview<Row> {
   }

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
            TableColumn<Row, Field>(column).apply {
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

      }

      center = table
   }
}