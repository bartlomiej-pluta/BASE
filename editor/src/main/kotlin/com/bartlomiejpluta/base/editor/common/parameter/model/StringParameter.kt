package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.Property
import javafx.scene.control.TextField

class StringParameter(
   key: String,
   initialValue: String = "",
   editable: Boolean = true,
   autocommit: Boolean = false,
   onCommit: (oldValue: String, newValue: String, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
) :
   Parameter<String>(key, initialValue, editable, autocommit, onCommit) {
   override val editor = TextField()
   override val editorValueProperty: Property<String>
      get() = editor.textProperty()

   init {
      super.init()
   }
}