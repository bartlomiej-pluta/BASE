package com.bartlomiejpluta.base.editor.common.parameter.model

import javafx.beans.property.*
import javafx.scene.Node
import tornadofx.getValue
import tornadofx.setValue

abstract class Parameter<T>(
   key: String,
   initialValue: T? = null,
   editable: Boolean = true,
   private val autocommit: Boolean = false,
   private val onCommit: (oldValue: T, newValue: T, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
) {
   val keyProperty = ReadOnlyStringWrapper(key)
   val key by keyProperty

   val editableProperty: BooleanProperty = SimpleBooleanProperty(editable)
   var editable by editableProperty

   val valueProperty: ObjectProperty<T> = SimpleObjectProperty(initialValue)
   var value by valueProperty

   fun commit() {
      if (!autocommit) {
         onCommit(value, editorValueProperty.value, this::submit)
      }
   }

   private fun submit() {
      value = editorValueProperty.value
   }

   protected fun init() {
      if (autocommit) {
         editorValueProperty.bindBidirectional(valueProperty)
         valueProperty.addListener { _, oldValue, newValue -> onCommit(oldValue, newValue, NOOP) }
      } else {
         editorValueProperty.value = value
         valueProperty.addListener { _, _, v -> editorValueProperty.value = v }
      }
   }

   abstract val editorValueProperty: Property<T>

   abstract val editor: Node

   open val valueString: String
      get() = value.toString()

   companion object {
      private val NOOP = {}
   }
}