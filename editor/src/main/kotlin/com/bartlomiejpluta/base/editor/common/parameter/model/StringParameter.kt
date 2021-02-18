package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.scene.control.TextField

class StringParameter(key: String, initialValue: String? = null, editable: Boolean = true) :
   Parameter<String>(key, initialValue, editable) {
   override val editor = TextField().apply { textProperty().bind(valueProperty) }
}