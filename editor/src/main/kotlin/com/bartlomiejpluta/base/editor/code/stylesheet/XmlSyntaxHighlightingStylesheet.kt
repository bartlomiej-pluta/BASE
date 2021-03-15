package com.bartlomiejpluta.base.editor.code.stylesheet

import javafx.scene.paint.Color
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import tornadofx.*


class XmlSyntaxHighlightingStylesheet : Stylesheet() {
   companion object {
      val prolog by cssclass()
      val namespace by cssclass()
      val tagmark by cssclass()
      val tagname by cssclass()
      val paren by cssclass()
      val attribute by cssclass()
      val value by cssclass()
      val comment by cssclass()
      val paragraphBox by cssclass()
      val paragraphText by cssclass()

      val hasCaret by csspseudoclass()

      val tabSize by cssproperty<Int>("-fx-tab-size")
   }

   init {
      prolog {
         fill = c("#BBB529")
         fontStyle = FontPosture.ITALIC
      }

      namespace {
         fill = Color.DARKVIOLET
      }

      tagmark {
         fill = Color.GRAY
      }

      tagname {
         fill = c("#000080")
         fontWeight = FontWeight.BOLD
      }

      paren {
         fill = Color.FIREBRICK
         fontWeight = FontWeight.BOLD
      }

      attribute {
         fill = Color.DARKSLATEBLUE
         fontStyle = FontPosture.ITALIC
      }

      value {
         fill = c("#008000")
      }

      comment {
         fill = c("#808080")
         fontStyle = FontPosture.ITALIC
      }

      paragraphText {
         tabSize.value = 3
      }
   }
}