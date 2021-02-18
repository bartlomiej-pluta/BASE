package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.scene.control.Spinner

class IntegerParameter(
   key: String,
   initialValue: Int,
   minValue: Int,
   maxValue: Int,
   editable: Boolean = true,
) : Parameter<Int>(key, initialValue, editable) {
   override val editor = Spinner<Int>(minValue, maxValue, initialValue).apply {
      isEditable = true
      valueFactory.valueProperty().bindBidirectional(valueProperty)
   }
}