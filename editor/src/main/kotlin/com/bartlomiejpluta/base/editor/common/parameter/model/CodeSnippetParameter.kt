package com.bartlomiejpluta.base.editor.common.parameter.model

import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.view.editor.CodeSnippetFragment
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.file.model.DummyFileNode
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import tornadofx.*

class CodeSnippetParameter(
   key: String,
   initialValue: String = "",
   codeType: CodeType = CodeType.JAVA,
   title: String = "Input code",
   prompt: String = "Edit...",
   editable: Boolean = true,
   onCommit: (oldValue: String, newValue: String, submit: () -> Unit) -> Unit = { _, _, submit -> submit() }
) : Parameter<String>(key, initialValue, editable, false, onCommit) {
   override val editorValueProperty = SimpleObjectProperty(initialValue)

   override val editor = Label(prompt).apply {
      addEventHandler(MouseEvent.MOUSE_CLICKED) {
         if (it.button == MouseButton.PRIMARY) {
            val scope = CodeScope(1, 1)
            val code = Code(DummyFileNode(), codeType.toProperty(), editorValueProperty.value)
            val vm = CodeVM(code)
            setInScope(vm, scope)

            find<CodeSnippetFragment>(scope).apply {
               this.title = title

               onComplete { code ->
                  editorValueProperty.value = code
                  commit()
               }

               openModal(block = true)
            }


            it.consume()
         }
      }
   }

   override val valueString = prompt

   init {
      super.init()
   }
}