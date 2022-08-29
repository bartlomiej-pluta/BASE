package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.binding.Bindings
import javafx.scene.control.CheckBox

class BooleanParameter(
   key: String,
   initialValue: Boolean = false,
   editable: Boolean = true,
   autocommit: Boolean = true,
   onCommit: (oldValue: Boolean, newValue: Boolean, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
) : Parameter<Boolean>(key, initialValue, editable, autocommit, onCommit, true) {
   override val editor = CheckBox()

   override val editorValueProperty = editor.selectedProperty()

   val booleanProperty = Bindings.createBooleanBinding({ value }, valueProperty)

   init {
      super.init()
   }
}