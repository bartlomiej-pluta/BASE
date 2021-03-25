package com.bartlomiejpluta.base.editor.code.view.editor

import com.bartlomiejpluta.base.editor.code.component.CodeEditor
import com.bartlomiejpluta.base.editor.code.highlighting.JavaSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.highlighting.SqlSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.highlighting.XmlSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.model.CodeScope
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.viewmodel.CodeVM
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.InMemoryStringFileNode
import com.bartlomiejpluta.base.editor.file.model.ScriptAssetFileNode
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import javafx.beans.binding.Bindings
import org.kordamp.ikonli.javafx.FontIcon
import tornadofx.*

class CodeEditorView : View() {
   override val scope = super.scope as CodeScope

   private val projectContext: ProjectContext by di()

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
      { codeVM.fileNode is FileSystemNode || codeVM.fileNode is ScriptAssetFileNode || codeVM.fileNode is InMemoryStringFileNode },
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

         if (codeVM.saveable && editable.value) {
            button(graphic = FontIcon("fa-floppy-o")) {
               enableWhen(codeVM.dirty)
               action { save() }
            }
         }

         button(graphic = FontIcon("fa-undo")) {
            enableWhen(editable)
            action { undo() }
         }

         button(graphic = FontIcon("fa-repeat")) {
            enableWhen(editable)
            action { redo() }
         }

         if (codeVM.executeProperty.isNotNull.value) {
            button(graphic = FontIcon("fa-play")) {
               action { execute() }
            }
         }
      }

      center = editor
   }

   fun redo() = editor.redo()

   fun undo() = editor.undo()

   fun save() = codeVM.takeIf { editable.value && it.saveable }?.item?.let {
      codeVM.commit(codeVM.codeProperty)
      projectContext.saveScript(it)
   }

   fun execute() {
      codeVM.commit(codeVM.codeProperty)
      codeVM.execute()
   }
}