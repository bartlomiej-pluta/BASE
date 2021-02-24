package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.code.component.CodeEditor
import com.bartlomiejpluta.base.editor.code.highlighting.JavaSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.command.context.UndoableScope
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class CodeEditorView : View() {
   override val scope = super.scope as UndoableScope

   private val projectContext: ProjectContext by di()

   private val javaSyntaxHighlighter: JavaSyntaxHighlighter by di()

   private val codeVM = find<CodeVM>()

   private val highlighter = Bindings.createObjectBinding({
      when (codeVM.type!!) {
         CodeType.JAVA -> javaSyntaxHighlighter
      }
   }, codeVM.typeProperty)

   private val editor = CodeEditor(highlighter, codeVM.codeProperty)

   fun shutdown() {
      editor.shutdownHighlighterThread()
   }

   override val root = borderpane {
      top = toolbar {
         button(graphic = FontIcon("fa-floppy-o")) {
            shortcut("Ctrl+S")
            enableWhen(codeVM.dirty)

            action {
               codeVM.item?.let {
                  codeVM.commit(codeVM.codeProperty)
                  projectContext.saveScript(it)
               }
            }
         }

         button(graphic = FontIcon("fa-undo")) {
            shortcut("Ctrl+Z")

            action {
               editor.undo()
            }
         }

         button(graphic = FontIcon("fa-repeat")) {
            shortcut("Ctrl+Shift+Z")

            action {
               editor.redo()
            }
         }
      }

      center = editor
   }
}