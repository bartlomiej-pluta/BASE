package com.bartlomiejpluta.base.editor.common.logs.component

import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.common.logs.style.LogsPaneStyle
import com.bartlomiejpluta.base.editor.common.logs.stylesheet.LogsPaneStylesheet
import javafx.scene.layout.StackPane
import org.codehaus.commons.compiler.Location
import org.fxmisc.richtext.StyledTextArea
import tornadofx.addClass

class LogsPane(private val locationClick: (location: Location) -> Unit = {}) : StackPane() {
   private val editor = StyledTextArea("logs-pane",
      { text, style -> text.addClass(style) },
      LogsPaneStyle.NO_STYLE,
      { text, style -> style.apply(text) }
   ).apply { isEditable = false }

   init {
      children += editor
   }

   fun appendEntry(
      message: String,
      severity: Severity,
      follow: Boolean,
      location: Location? = null,
      tag: String? = null
   ) {
      val locationRef = LogsPaneStyle(location = location, onClick = locationClick)
      val severityStyle = LogsPaneStyle(severity = severity)

      tag?.let { editor.insert(editor.length, "[$it] ", severityStyle) }
      editor.insert(editor.length, location?.toString() ?: "", locationRef)
      editor.insert(editor.length, (location?.let { ": " } ?: "") + message, LogsPaneStyle(severity = severity))
      editor.insert(editor.length, "\n", LogsPaneStyle.NO_STYLE)

      if (follow) {
         editor.requestFollowCaret()
      }
   }

   fun clear() = editor.clear()

   override fun getUserAgentStylesheet(): String = LogsPaneStylesheet().base64URL.toExternalForm()
}