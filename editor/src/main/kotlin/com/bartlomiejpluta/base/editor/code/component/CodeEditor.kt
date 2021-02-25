package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.code.highlighting.SyntaxHighlighter
import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.concurrent.Task
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.StackPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors


class CodeEditor(private val highlighter: ObservableValue<out SyntaxHighlighter>, codeProperty: Property<String>) :
   StackPane() {
   private val editor = CodeArea()
   private val executor = Executors.newSingleThreadExecutor()

   private val highlightingSubscription = editor.multiPlainChanges()
      .successionEnds(Duration.ofMillis(500))
      .supplyTask(this::computeHighlightingAsync)
      .awaitLatest(editor.multiPlainChanges())
      .filterMap {
         when {
            it.isSuccess -> Optional.of(it.get())
            else -> Optional.empty()
         }
      }
      .subscribe(this::applyHighlighting)

   init {
      editor.replaceText(0, 0, codeProperty.value)
      codeProperty.bind(editor.textProperty())
      editor.paragraphGraphicFactory = LineNumberFactory.get(editor)
      applyHighlighting(highlighter.value.highlight(editor.text))

      initAutoIndents()

      children += VirtualizedScrollPane(editor)
   }

   fun undo() {
      editor.undo()
   }

   fun redo() {
      editor.redo()
   }

   fun shutdownHighlighterThread() {
      highlightingSubscription.unsubscribe()
      executor.shutdownNow()
      editor.dispose()
   }

   fun setCaretPosition(line: Int, column: Int) {
      editor.moveTo(line - 1, column - 1)
      editor.requestFollowCaret()
   }

   private fun initAutoIndents() {
      editor.addEventHandler(KeyEvent.KEY_PRESSED) { event ->
         if (event.code === KeyCode.ENTER) {
            WHITESPACE.find(editor.getParagraph(editor.currentParagraph - 1).segments[0])?.apply {
               editor.insertText(editor.caretPosition, value)
            }
         }
      }
   }

   private fun computeHighlightingAsync(): Task<StyleSpans<Collection<String>>> {
      val code = editor.text

      val task = object : Task<StyleSpans<Collection<String>>>() {
         override fun call() = highlighter.value.highlight(code)
      }

      executor.execute(task)
      return task
   }

   private fun applyHighlighting(highlighting: StyleSpans<Collection<String>>) {
      editor.setStyleSpans(0, highlighting)
   }

   override fun getUserAgentStylesheet(): String = highlighter.value.stylesheet.base64URL.toExternalForm()

   companion object {
      private val WHITESPACE = "^\\s+".toRegex()
   }
}