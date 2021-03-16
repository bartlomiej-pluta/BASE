package com.bartlomiejpluta.base.editor.code.stylesheet

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.cssclass
import tornadofx.px

open class CodeEditorStylesheet : Stylesheet() {
   companion object {
      val paragraphText by cssclass()
   }

   init {
      paragraphText {
         fontSize = 14.px
         fontWeight = FontWeight.MEDIUM
      }
   }
}