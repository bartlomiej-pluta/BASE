package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.Property
import javafx.scene.control.ComboBox

class EnumParameter<E : Enum<E>>(
   key: String,
   initialValue: E,
   editable: Boolean = true,
   autocommit: Boolean = false
) : Parameter<E>(key, initialValue, editable, autocommit) {
   override val editor = ComboBox<E>().apply { items.setAll(initialValue.javaClass.enumConstants.toList()) }
   override val editorValueProperty: Property<E>
      get() = editor.valueProperty()

   init {
      super.init()
   }
}