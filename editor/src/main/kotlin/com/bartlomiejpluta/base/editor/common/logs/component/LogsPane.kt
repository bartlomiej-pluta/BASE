package com.bartlomiejpluta.base.editor.common.logs.component

import com.bartlomiejpluta.base.editor.common.logs.style.LogsPaneStyle
import com.bartlomiejpluta.base.editor.common.logs.stylesheet.LogsPaneStylesheet
import javafx.scene.layout.StackPane
import org.fxmisc.flowless.VirtualizedScrollPane
import org.fxmisc.richtext.StyledTextArea
import tornadofx.addClass
import tornadofx.runLater
import java.io.OutputStream

class LogsPane : StackPane() {
   private val editor = StyledTextArea("logs-pane",
      { text, style -> text.addClass(style) },
      LogsPaneStyle.DEFAULT,
      { text, style -> style.apply(text) }
   ).apply { isEditable = false }

   init {
      children += VirtualizedScrollPane(editor)
   }

   fun clear() = editor.clear()

   override fun getUserAgentStylesheet(): String = LogsPaneStylesheet().base64URL.toExternalForm()

   val stdout = object : OutputStream() {
      override fun write(b: Int) {
         runLater {
            editor.insert(editor.length, (b.toChar()).toString(), LogsPaneStyle.DEFAULT)
            editor.requestFollowCaret()
         }
      }
   }

   val stderr = object : OutputStream() {
      override fun write(b: Int) {
         runLater {
            editor.insert(editor.length, (b.toChar()).toString(), LogsPaneStyle.ERROR)
            editor.requestFollowCaret()
         }
      }
   }
}