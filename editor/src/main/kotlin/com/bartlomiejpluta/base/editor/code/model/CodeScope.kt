package com.bartlomiejpluta.base.editor.code.model

import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import tornadofx.toProperty

class CodeScope(line: Int, column: Int) : UndoableScope() {
   private val requestCaretPositionProperty = (line to column).toProperty()

   fun setCaretPosition(line: Int, column: Int) {
      requestCaretPositionProperty.value = line to column
   }

   fun addCaretDisplacementRequestListener(listener: (line: Int, column: Int) -> Unit) {
      requestCaretPositionProperty.addListener { _, _, position -> listener(position.first, position.second) }
      listener(requestCaretPositionProperty.value.first, requestCaretPositionProperty.value.second)
   }
}