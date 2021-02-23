package com.bartlomiejpluta.base.editor.code.stylesheet

import javafx.scene.text.FontWeight
import tornadofx.*

class HighlightingStylesheet : Stylesheet() {
   companion object {
      val keyword by cssclass()
      val semicolon by cssclass()
      val paren by cssclass()
      val bracket by cssclass()
      val brace by cssclass()
      val string by cssclass()
      val comment by cssclass()
      val paragraphBox by cssclass()
      val hasCaret by csspseudoclass()
   }

   init {
      keyword {
         fill = c("purple")
         fontWeight = FontWeight.BOLD
      }

      semicolon {
         fontWeight = FontWeight.BOLD
      }

      paren {
         fill = c("firebrick")
         fontWeight = FontWeight.BOLD
      }

      bracket {
         fill = c("darkgreen")
         fontWeight = FontWeight.BOLD
      }

      brace {
         fill = c("teal")
         fontWeight = FontWeight.BOLD
      }

      string {
         fill = c("blue")
      }

      comment {
         fill = c("cadetblue")
      }

      paragraphBox and hasCaret {
         backgroundColor = multi(c("#f2f9fc"))
      }
   }
}