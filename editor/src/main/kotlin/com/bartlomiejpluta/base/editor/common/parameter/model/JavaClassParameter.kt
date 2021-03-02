package com.bartlomiejpluta.base.editor.common.parameter.model

import com.bartlomiejpluta.base.editor.code.view.select.SelectJavaClassFragment
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.Scope
import tornadofx.find
import tornadofx.toProperty
import tornadofx.tooltip

class JavaClassParameter(
   key: String,
   initialValue: String = "",
   editable: Boolean = true,
   onCommit: (oldValue: String, newValue: String, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
) : Parameter<String>(key, initialValue, editable, false, onCommit) {
   override val editorValueProperty = initialValue.toProperty()

   override val editor = Label(initialValue).apply {
      textProperty().bind(editorValueProperty)

      tooltip {
         textProperty().bind(editorValueProperty)
      }

      addEventHandler(MouseEvent.MOUSE_CLICKED) {
         if (it.button == MouseButton.PRIMARY) {
            find<SelectJavaClassFragment>(Scope()).apply {
               onComplete { className ->
                  editorValueProperty.value = className
                  commit()
               }

               openModal(block = true, resizable = false)
            }

            it.consume()
         }
      }
   }

   init {
      super.init()
   }
}