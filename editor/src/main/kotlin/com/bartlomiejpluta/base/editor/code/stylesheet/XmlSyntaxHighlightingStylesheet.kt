package com.bartlomiejpluta.base.editor.code.stylesheet

import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.cssclass


class XmlSyntaxHighlightingStylesheet : Stylesheet() {
   companion object {
      val prolog by cssclass()
      val namespace by cssclass()
      val tagmark by cssclass()
      val anytag by cssclass()
      val paren by cssclass()
      val attribute by cssclass()
      val avalue by cssclass()
      val comment by cssclass()
   }

   init {
      prolog {
         fill = c("#BBB529")
         fontStyle = FontPosture.ITALIC
      }

      namespace {
         fill = Color.DARKRED
      }

      tagmark {
         fill = Color.GRAY
      }

      anytag {
         fill = Color.CRIMSON
      }

      paren {
         fill = Color.FIREBRICK
         fontWeight = FontWeight.BOLD
      }

      attribute {
         fill = Color.DARKVIOLET
      }

      avalue {
         fill = Color.BLACK
      }

      comment {
         fill = Color.TEAL
      }
   }
}