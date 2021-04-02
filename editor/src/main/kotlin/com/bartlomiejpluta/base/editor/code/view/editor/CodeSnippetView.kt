package com.bartlomiejpluta.base.editor.code.view.editor

import com.bartlomiejpluta.base.editor.code.component.CodeEditor
import com.bartlomiejpluta.base.editor.code.highlighting.JavaSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.highlighting.SqlSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.highlighting.XmlSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.file.model.DummyFileNode
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.InMemoryStringFileNode
import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import javafx.beans.binding.Bindings
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class CodeSnippetView : View() {
   override val scope = super.scope as CodeScope

   private val javaSyntaxHighlighter: JavaSyntaxHighlighter by di()
   private val xmlSyntaxHighlighter: XmlSyntaxHighlighter by di()
   private val sqlSyntaxHighlighter: SqlSyntaxHighlighter by di()

   private val codeVM = find<CodeVM>()

   private val highlighter = Bindings.createObjectBinding({
      when (codeVM.type!!) {
         CodeType.JAVA -> javaSyntaxHighlighter
         CodeType.XML -> xmlSyntaxHighlighter
         CodeType.SQL -> sqlSyntaxHighlighter
      }
   }, codeVM.typeProperty)

   private val editable = Bindings.createBooleanBinding(
      { codeVM.fileNode is FileSystemNode || codeVM.fileNode is ScriptAssetFileNode || codeVM.fileNode is InMemoryStringFileNode || codeVM.fileNode is DummyFileNode },
      codeVM.itemProperty
   )

   private val editor = CodeEditor(highlighter, codeVM.codeProperty, !editable.value)

   init {
      scope.caretDisplacementRequestListener = { line, column ->
         editor.setCaretPosition(line, column)
      }
   }

   fun shutdown() {
      editor.shutdownHighlighterThread()
      codeVM.fileNode
   }

   override val root = borderpane {
      top = toolbar {

         button(graphic = FontIcon("fa-undo")) {
            enableWhen(editable)
            action { undo() }
         }

         button(graphic = FontIcon("fa-repeat")) {
            enableWhen(editable)
            action { redo() }
         }
      }

      center = editor
   }

   fun redo() = editor.redo()

   fun undo() = editor.undo()
}