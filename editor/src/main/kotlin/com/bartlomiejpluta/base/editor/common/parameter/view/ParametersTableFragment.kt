package com.bartlomiejpluta.base.editor.common.parameter.view

import com.bartlomiejpluta.base.editor.common.parameter.component.ParameterValueEditingCell
import com.bartlomiejpluta.base.editor.common.parameter.model.Parameter
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import tornadofx.*

class ParametersTableFragment : Fragment() {
   val parameters: ObservableList<Parameter<*>> by param()

   override val root = tableview(parameters) {
      isEditable = true
      columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY

      column("Key", Parameter<*>::keyProperty)
      TableColumn<Parameter<*>, Any>("Value").apply {
         setCellValueFactory {
            @Suppress("UNCHECKED_CAST")
            it.value.valueProperty as ObservableValue<Any>
         }
         setCellFactory { ParameterValueEditingCell() }
      }.let { addColumnInternal(it) }
   }
}