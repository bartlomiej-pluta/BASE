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
   private var other: Property<T>? = null

   val keyProperty = ReadOnlyStringWrapper(key)

   val key by keyProperty
   val editableProperty: BooleanProperty = SimpleBooleanProperty(editable)

   var editable by editableProperty
   val valueProperty: ObjectProperty<T> = SimpleObjectProperty(initialValue)

   var value by valueProperty

   fun commit() = onCommit(value, editorValueProperty.value, if (autocommit) NOOP else this::submit)

   fun bindBidirectional(other: Property<T>) {
      unbindBidirectional()
      this.value = other.value
      this.other = other.apply { bindBidirectional(valueProperty) }
   }

   fun unbindBidirectional() {
      this.other?.unbindBidirectional(valueProperty)
      this.other = null
   }

   private fun submit() {
      value = editorValueProperty.value
   }

   protected fun init() {
      if (autocommit) {
         editorValueProperty.bindBidirectional(valueProperty)
         valueProperty.addListener { _, oldValue, newValue ->

            // Disclaimer:
            // This ugly hack enforces the onCommit() listener to be fired, when
            // `other` Property has been updated with current value.
            // Without that hack, even if `other` is bidirectionally bound to `valueProperty`,
            // it is updated right after the onCommit() listener, which is not very useful.
            other?.value = newValue

            onCommit(oldValue, newValue, NOOP)
         }
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