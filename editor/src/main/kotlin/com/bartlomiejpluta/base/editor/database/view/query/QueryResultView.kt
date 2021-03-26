package com.bartlomiejpluta.base.editor.database.view.query

import com.bartlomiejpluta.base.editor.database.controller.DatabaseController
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import javafx.scene.control.TableColumn
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class QueryResultView : View() {
   private val databaseController: DatabaseController by di()
   private val queryVM = find<QueryVM>()

   private val table = tableview(queryVM.dataProperty)

   init {
      updateColumns()

      queryVM.itemProperty.addListener { _, _, query ->
         updateColumns()
      }
   }

   private fun updateColumns() {
      table.columns.clear()
      queryVM.columns.map { column ->
         TableColumn<DataRecord, String>(column).apply {
            setCellValueFactory {
               it.value.fields[column]!!.valueProperty
            }
         }
      }.let(table.columns::addAll)
   }

   override val root = borderpane {
      top = toolbar {
         button(graphic = FontIcon("fa-refresh")) {
            action {
               databaseController.execute(queryVM.query, queryVM.name)?.let {
                  queryVM.item = it
               }
            }
         }

         button(graphic = FontIcon("fa-plus")) {
            action {
               queryVM.item?.addEmptyRecord()
            }
         }
      }

      center = table
   }
}