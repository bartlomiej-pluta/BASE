package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.Property
import javafx.scene.control.Spinner

class IntegerParameter(
   key: String,
   initialValue: Int,
   minValue: Int,
   maxValue: Int,
   editable: Boolean = true,
   autocommit: Boolean = false,
   onCommit: (oldValue: Int, newValue: Int, submit: () -> Unit) -> Unit = { _, _, submit -> submit() },
) : Parameter<Int>(key, initialValue, editable, autocommit, onCommit) {
   override val editor = Spinner<Int>(minValue, maxValue, initialValue).apply { isEditable = true }
   override val editorValueProperty: Property<Int>
      get() = editor.valueFactory.valueProperty()

   init {
      super.init()
   }
}