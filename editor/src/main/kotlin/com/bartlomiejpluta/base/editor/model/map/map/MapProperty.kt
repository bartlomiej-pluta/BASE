package com.bartlomiejpluta.base.editor.model.map.map

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class MapProperty(key: String, value: Int) {
   val keyProperty = SimpleStringProperty(key)
   val key by keyProperty

   val valueProperty = SimpleIntegerProperty(value)
   val value by valueProperty
}