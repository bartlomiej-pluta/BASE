package com.bartlomiejpluta.base.editor.code.component

import com.bartlomiejpluta.base.editor.code.stylesheet.CompilerLogsStylesheet
import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import javafx.scene.Cursor
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.codehaus.commons.compiler.Location
import org.fxmisc.richtext.StyledTextArea
import tornadofx.addClass

class CompilationLogs(private val locationClick: (location: Location) -> Unit) : StackPane() {
   private val editor = StyledTextArea("compiler-logs",
      { text, style -> text.addClass(style) },
      CompilationLogStyle.NO_STYLE,
      { text, style -> style.apply(text) }
   ).apply { isEditable = false }

   init {
      children += editor
   }

   fun setEntry(message: String, severity: UpdateCompilationLogEvent.Severity, location: Location?) {
      editor.clear()

      val locationRef = CompilationLogStyle(location = location, onClick = locationClick)

      editor.insert(editor.length, location?.toString() ?: "", locationRef)
      editor.insert(editor.length, message, CompilationLogStyle(severity = severity))
   }

   fun clear() = editor.clear()

   override fun getUserAgentStylesheet(): String = CompilerLogsStylesheet().base64URL.toExternalForm()

   class CompilationLogStyle(
      private val location: Location? = null,
      private val severity: UpdateCompilationLogEvent.Severity? = null,
      private val onClick: (Location) -> Unit = {}
   ) {

      fun apply(text: Text) = when {
         severity != null -> message(text, severity)
         location != null -> location(text, location)
         else -> {
         }
      }

      private fun message(text: Text, severity: UpdateCompilationLogEvent.Severity) {
         text.fill = when (severity) {
            UpdateCompilationLogEvent.Severity.WARNING -> Color.ORANGE
            UpdateCompilationLogEvent.Severity.ERROR -> Color.RED
            UpdateCompilationLogEvent.Severity.INFO -> text.fill
         }
      }

      private fun location(text: Text, location: Location) {
         text.cursor = Cursor.HAND
         text.fill = Color.BLUE
         text.isUnderline = true
         text.setOnMouseClicked {
            onClick(location)
         }
      }

      companion object {
         val NO_STYLE = CompilationLogStyle()
      }
   }
}