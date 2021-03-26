package com.bartlomiejpluta.base.editor.database.model.data

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class DataField(value: String?) {
   lateinit var record: DataRecord

   val valueProperty = SimpleStringProperty(value)
   var value by valueProperty
}