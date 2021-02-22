package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.DoubleProperty
import javafx.beans.property.Property
import javafx.scene.control.Spinner

class DoubleParameter(
   key: String,
   initialValue: Double,
   minValue: Double,
   maxValue: Double,
   step: Double,
   editable: Boolean = true,
   autocommit: Boolean = false,
   onCommit: (oldValue: Double, newValue: Double, submit: () -> Unit) -> Unit = { _, _, submit -> submit() },
) : Parameter<Double>(key, initialValue, editable, autocommit, onCommit) {
   override val editor = Spinner<Double>(minValue, maxValue, initialValue, step).apply {
      isEditable = true
   }

   constructor(
      key: String,
      initialValue: Double,
      editable: Boolean = true,
      autocommit: Boolean = false,
      onCommit: (oldValue: Double, newValue: Double, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
   ) : this(key, initialValue, Double.MIN_VALUE, Double.MAX_VALUE, 0.1, editable, autocommit, onCommit)

   override val editorValueProperty: Property<Double>
      get() = editor.valueFactory.valueProperty()

   init {
      super.init()
   }

   fun bindBidirectional(other: DoubleProperty) {
      super.bindBidirectional(other.asObject())
   }
}