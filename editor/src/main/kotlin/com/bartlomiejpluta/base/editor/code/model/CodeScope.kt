package com.bartlomiejpluta.base.editor.code.model

import com.bartlomiejpluta.base.editor.command.context.UndoableScope

class CodeScope(private var line: Int, private var column: Int) : UndoableScope() {
   var caretDisplacementRequestListener: ((line: Int, column: Int) -> Unit)? = null
      set(value) {
         field = value
         field?.let { it(line, column) }
      }

   fun setCaretPosition(line: Int, column: Int) {
      this.line = line
      this.column = column

      caretDisplacementRequestListener?.let { it(line, column) }
   }
}