package com.bartlomiejpluta.base.editor.common.logs.stylesheet

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.cssclass

class LogsPaneStylesheet : Stylesheet() {
   companion object {
      val logsPane by cssclass()
   }

   init {
      logsPane {
         fontFamily = "monospace"
         fontWeight = FontWeight.MEDIUM
      }
   }
}