package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.Property
import javafx.scene.control.TextField

class StringParameter(
   key: String,
   initialValue: String? = null,
   editable: Boolean = true,
   autocommit: Boolean = false
) :
   Parameter<String>(key, initialValue, editable, autocommit) {
   override val editor = TextField()
   override val editorValueProperty: Property<String>
      get() = editor.textProperty()

   init {
      super.init()
   }
}