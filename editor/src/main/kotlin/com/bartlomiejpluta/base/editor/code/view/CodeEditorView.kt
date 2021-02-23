package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.code.component.CodeEditor
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import tornadofx.View
import tornadofx.borderpane

class CodeEditorView : View() {
   private val codeVM = find<CodeVM>()
   private val editor = CodeEditor(codeVM.codeProperty)

   override val root = borderpane {
      center = editor
   }
}