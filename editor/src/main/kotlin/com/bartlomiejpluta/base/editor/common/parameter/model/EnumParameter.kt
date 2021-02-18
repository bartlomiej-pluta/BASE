package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.scene.control.ComboBox

class EnumParameter<E : Enum<E>>(
   key: String,
   initialValue: E,
   editable: Boolean = true
) : Parameter<E>(key, initialValue, editable) {
   override val editor = ComboBox<E>().apply {
      items.setAll(initialValue.javaClass.enumConstants.toList())
      valueProperty().bindBidirectional(valueProperty)
   }
}