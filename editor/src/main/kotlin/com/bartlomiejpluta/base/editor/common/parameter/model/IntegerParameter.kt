package com.bartlomiejpluta.base.editor.common.parameter.model

import com.bartlomiejpluta.base.editor.util.fx.TextFieldUtil
import javafx.beans.property.IntegerProperty
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
   override val editor = Spinner<Int>(minValue, maxValue, initialValue).apply {
      isEditable = true
      editor.textFormatter = TextFieldUtil.integerFormatter(initialValue)
   }

   constructor(
      key: String,
      initialValue: Int,
      editable: Boolean = true,
      autocommit: Boolean = false,
      onCommit: (oldValue: Int, newValue: Int, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
   ) : this(key, initialValue, Integer.MIN_VALUE, Integer.MAX_VALUE, editable, autocommit, onCommit)

   override val editorValueProperty: Property<Int>
      get() = editor.valueFactory.valueProperty()

   init {
      super.init()
   }

   fun bindBidirectional(other: IntegerProperty) {
      super.bindBidirectional(other.asObject())
   }
}