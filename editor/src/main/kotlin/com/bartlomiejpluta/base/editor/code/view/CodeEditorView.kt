package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.code.component.CodeEditor
import com.bartlomiejpluta.base.editor.code.highlighting.JavaSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import javafx.beans.binding.Bindings
import tornadofx.View
import tornadofx.borderpane

class CodeEditorView : View() {
   private val javaSyntaxHighlighter: JavaSyntaxHighlighter by di()
   private val codeVM = find<CodeVM>()

   private val highlighter = Bindings.createObjectBinding({
      when (codeVM.type!!) {
         CodeType.JAVA -> javaSyntaxHighlighter
      }
   }, codeVM.typeProperty)

   private val editor = CodeEditor(highlighter, codeVM.codeProperty)

   override val root = borderpane {
      center = editor
   }
}