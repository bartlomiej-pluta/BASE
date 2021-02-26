package com.bartlomiejpluta.base.editor.common.logs.style

import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import javafx.scene.Cursor
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.codehaus.commons.compiler.Location

class LogsPaneStyle(
   private val location: Location? = null,
   private val severity: Severity? = null,
   private val onClick: (Location) -> Unit = {}
) {

   fun apply(text: Text) = when {
      severity != null -> message(text, severity)
      location != null -> location(text, location)
      else -> {
      }
   }

   private fun message(text: Text, severity: Severity) {
      text.fill = when (severity) {
         Severity.WARNING -> Color.ORANGE
         Severity.ERROR -> Color.RED
         Severity.INFO -> text.fill
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
      val NO_STYLE = LogsPaneStyle()
   }
}