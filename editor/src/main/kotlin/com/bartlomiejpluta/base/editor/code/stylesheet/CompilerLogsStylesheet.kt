package com.bartlomiejpluta.base.editor.code.stylesheet

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.cssclass

class CompilerLogsStylesheet : Stylesheet() {
   companion object {
      val compilerLogs by cssclass()
   }

   init {
      compilerLogs {
         fontFamily = "monospace"
         fontWeight = FontWeight.MEDIUM
      }
   }
}