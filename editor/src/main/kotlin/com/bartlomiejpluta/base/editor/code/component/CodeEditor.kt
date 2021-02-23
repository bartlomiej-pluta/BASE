package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.code.highlighting.JavaSyntaxHighlighter
import com.bartlomiejpluta.base.editor.code.stylesheet.HighlightingStylesheet
import javafx.beans.property.Property
import javafx.concurrent.Task
import javafx.scene.layout.StackPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import org.fxmisc.richtext.model.StyleSpans
import java.time.Duration
import java.util.*
import java.util.concurrent.Executors


class CodeEditor(val codeProperty: Property<String>) : StackPane() {
   private val editor = CodeArea()
   private val executor = Executors.newSingleThreadExecutor()
   private val cleanupWhenDone = editor.multiPlainChanges()

   private val highlighting = JavaSyntaxHighlighter()

   init {
      editor.paragraphGraphicFactory = LineNumberFactory.get(editor)
      editor.replaceText(0, 0, codeProperty.value)
      applyHighlighting(highlighting.highlight(editor.text))

      cleanupWhenDone
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


      children += VirtualizedScrollPane(editor)
   }

   private fun computeHighlightingAsync(): Task<StyleSpans<Collection<String>>> {
      val code = editor.text

      val task = object : Task<StyleSpans<Collection<String>>>() {
         override fun call() = highlighting.highlight(code)
      }

      executor.execute(task)
      return task
   }

   private fun applyHighlighting(highlighting: StyleSpans<Collection<String>>) {
      editor.setStyleSpans(0, highlighting)
   }

   override fun getUserAgentStylesheet() = HighlightingStylesheet().base64URL.toExternalForm()
}