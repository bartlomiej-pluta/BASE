package com.bartlomiejpluta.base.editor.database.view.query

import com.bartlomiejpluta.base.editor.database.component.DataFieldStringConverter
import com.bartlomiejpluta.base.editor.database.component.DataRecordRow
import com.bartlomiejpluta.base.editor.database.controller.DatabaseController
import com.bartlomiejpluta.base.editor.database.model.data.DataField
import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Operation
import com.bartlomiejpluta.base.editor.database.viewmodel.QueryVM
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.TextFieldTableCell
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class QueryResultView : View() {
   private val databaseController: DatabaseController by di()
   private val queryVM = find<QueryVM>()

   private val table = tableview(queryVM.dataProperty) {
      editableProperty().bind(queryVM.tableProperty.isNotNull)

      setRowFactory { DataRecordRow() }
   }

   init {
      updateColumns()

      queryVM.itemProperty.addListener { _, _, query ->
         updateColumns()
      }
   }

   private fun updateColumns() {
      table.columns.clear()
      queryVM.columns.map { column ->
         TableColumn<DataRecord, DataField>(column).apply {
            setCellFactory { column ->
               val converter = DataFieldStringConverter()
               val cell = TextFieldTableCell.forTableColumn<DataRecord, DataField>(converter).call(column)
               converter.cell = cell
               cell
            }

            setCellValueFactory {
               it.value.fields[column]!!.toProperty()
            }
         }
      }.let(table.columns::addAll)
   }

   override val root = borderpane {
      top = toolbar {
         button(graphic = FontIcon("fa-refresh")) {
            action {
               databaseController.execute(queryVM.query, queryVM.name, queryVM.table)?.let {
                  queryVM.item = it
               }
            }
         }

         button(graphic = FontIcon("fa-plus")) {
            enableWhen(queryVM.tableProperty.isNotNull)

            action {
               queryVM.item?.addEmptyRecord()
            }
         }

         button(graphic = FontIcon("fa-minus")) {
            enableWhen(queryVM.tableProperty.isNotNull)

            action {
               val selected = table.selectionModel.selectedItem
               when (selected.operation) {
                  Operation.INSERT -> table.items.remove(selected)
                  else -> selected.operation = Operation.DELETE
               }
            }
         }

         button(graphic = FontIcon("fa-check")) {
            enableWhen(queryVM.tableProperty.isNotNull)

            action {
               println(queryVM.data.size)
            }
         }
      }

      center = table
   }
}