package com.bartlomiejpluta.base.editor.code.view

import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import org.fxmisc.richtext.CodeArea
import tornadofx.View
import tornadofx.enableWhen
import tornadofx.toProperty

class CompilerLogsView : View() {
   private val editor = CodeArea().apply {
      enableWhen(false.toProperty())
   }

   init {
      subscribe<UpdateCompilationLogEvent> {
         editor.clear()
         editor.replaceText(0, 0, it.message)
      }
   }

   override val root = editor
}