package com.bartlomiejpluta.base.editor.code.stylesheet

import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import tornadofx.*

class JavaSyntaxHighlightingStylesheet : Stylesheet() {
   companion object {
      val keyword by cssclass()
      val semicolon by cssclass()
      val paren by cssclass()
      val bracket by cssclass()
      val brace by cssclass()
      val string by cssclass()
      val number by cssclass()
      val annotation by cssclass()
      val operator by cssclass()
      val field by cssclass()
      val comment by cssclass()
      val paragraphBox by cssclass()
      val paragraphText by cssclass()

      val hasCaret by csspseudoclass()

      val tabSize by cssproperty<Int>("-fx-tab-size")
   }

   init {
      keyword {
         fill = c("#000080")
         fontWeight = FontWeight.BOLD
      }

      semicolon {
         fontWeight = FontWeight.BOLD
      }

      paren {
         fill = c("cadetblue")
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
         fill = c("#008000")
      }

      number {
         fill = c("#0000FF")
      }

      operator {
         fill = c("#CC7832")
      }

      annotation {
         fill = c("#BBB529")
         fontWeight = FontWeight.BOLD
      }

      comment {
         fill = c("#808080")
         fontStyle = FontPosture.ITALIC
      }

      paragraphBox and hasCaret {
         backgroundColor = multi(c("#f2f9fc"))
      }

      paragraphText {
         tabSize.value = 3
      }
   }
}