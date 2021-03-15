package com.bartlomiejpluta.base.editor.code.view.editor

import com.bartlomiejpluta.base.editor.code.component.CodeEditor
import com.bartlomiejpluta.base.editor.code.highlighting.JavaSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class CodeEditorView : View() {
   override val scope = super.scope as CodeScope

   private val projectContext: ProjectContext by di()

   private val javaSyntaxHighlighter: JavaSyntaxHighlighter by di()

   private val codeVM = find<CodeVM>()

   private val highlighter = Bindings.createObjectBinding({
      when (codeVM.type!!) {
         CodeType.JAVA -> javaSyntaxHighlighter
         CodeType.XML -> javaSyntaxHighlighter
      }
   }, codeVM.typeProperty)

   private val editable = Bindings.createBooleanBinding(
      { codeVM.fileNode is FileSystemNode || codeVM.fileNode is ScriptAssetFileNode },
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
         button(graphic = FontIcon("fa-floppy-o")) {
            enableWhen(codeVM.dirty.and(editable))
            action { save() }
         }

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

   fun save() = codeVM.item?.let {
      codeVM.commit(codeVM.codeProperty)
      projectContext.saveScript(it)
   }
}