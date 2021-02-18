package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.*
import javafx.scene.Node
import tornadofx.getValue
import tornadofx.setValue

abstract class Parameter<T>(key: String, initialValue: T? = null, editable: Boolean = true) {
   val keyProperty = ReadOnlyStringWrapper(key)
   val key by keyProperty

   val editableProperty: BooleanProperty = SimpleBooleanProperty(editable)
   var editable by editableProperty

   val valueProperty: ObjectProperty<T> = SimpleObjectProperty(initialValue)
   var value by valueProperty

   abstract val editor: Node

   open val valueString: String
      get() = value.toString()
}