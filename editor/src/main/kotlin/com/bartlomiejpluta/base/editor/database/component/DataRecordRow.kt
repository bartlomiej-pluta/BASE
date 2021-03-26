package com.bartlomiejpluta.base.editor.database.component

import com.bartlomiejpluta.base.editor.database.model.data.DataRecord
import com.bartlomiejpluta.base.editor.database.model.data.Operation
import javafx.beans.binding.Bindings
import javafx.scene.control.TableRow

class DataRecordRow : TableRow<DataRecord>() {
   override fun updateItem(item: DataRecord?, empty: Boolean) {
      super.updateItem(item, empty)
      when {
         empty || item == null -> {
            text = null
            graphic = null
            styleProperty().unbind()
            style = ""
         }

         else -> {
            styleProperty().bind(
               Bindings.createStringBinding({
                  when (item.operation) {
                     Operation.INSERT -> "-fx-background-color: $INSERT_BG;"
                     Operation.UPDATE -> "-fx-background-color: $UPDATE_BG;"
                     Operation.DELETE -> "-fx-background-color: $DELETE_BG;"
                     else -> ""
                  }
               }, item.operationProperty)
            )
         }
      }
   }

   companion object {
      private const val INSERT_BG = "#00AA00"
      private const val INSERT_FG = "#FFFFFF"

      private const val UPDATE_BG = "#AABB00"
      private const val UPDATE_FG = "#FFFFFF"

      private const val DELETE_BG = "#FF0000"
      private const val DELETE_FG = "#FFFFFF"
   }
}