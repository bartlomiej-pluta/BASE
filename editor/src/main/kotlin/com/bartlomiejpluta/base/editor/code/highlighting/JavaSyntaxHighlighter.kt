package com.bartlomiejpluta.base.editor.code.highlighting

import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder

class JavaSyntaxHighlighter : SyntaxHighlighter {
   override fun highlight(code: String): StyleSpans<Collection<String>> = StyleSpansBuilder<Collection<String>>().let {
      val lastKeywordEnd = PATTERN.findAll(code).fold(0) { lastKeywordEnd, result ->
         val styleClass = when {
            result.groups["KEYWORD"] != null -> "keyword"
            result.groups["PAREN"] != null -> "paren"
            result.groups["BRACE"] != null -> "brace"
            result.groups["BRACKET"] != null -> "bracket"
            result.groups["SEMICOLON"] != null -> "semicolon"
            result.groups["STRING"] != null -> "string"
            result.groups["COMMENT"] != null -> "comment"
            else -> throw IllegalStateException("Unsupported regex group")
         }

         it.add(emptyList(), result.range.first - lastKeywordEnd)
         it.add(listOf(styleClass), result.range.last - result.range.first + 1)

         result.range.last + 1
      }

      it.add(emptyList(), code.length - lastKeywordEnd)

      it.create()
   }

   companion object {
      private val KEYWORDS = arrayOf(
         "abstract", "assert", "boolean", "break", "byte",
         "case", "catch", "char", "class", "const",
         "continue", "default", "do", "double", "else",
         "enum", "extends", "final", "finally", "float",
         "for", "goto", "if", "implements", "import",
         "instanceof", "int", "interface", "long", "native",
         "new", "package", "private", "protected", "public",
         "return", "short", "static", "strictfp", "super",
         "switch", "synchronized", "this", "throw", "throws",
         "transient", "try", "void", "volatile", "while"
      )

      private val KEYWORD_PATTERN = "\\b(" + KEYWORDS.joinToString("|") + ")\\b"
      private val PAREN_PATTERN = "\\(|\\)"
      private val BRACE_PATTERN = "\\{|\\}"
      private val BRACKET_PATTERN = "\\[|\\]"
      private val SEMICOLON_PATTERN = "\\;"
      private val STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\""
      private val COMMENT_PATTERN = """
      //[^
      ]*|/\*(.|\R)*?\*/
      """.trimIndent()

      private val PATTERN = (
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                  + "|(?<PAREN>" + PAREN_PATTERN + ")"
                  + "|(?<BRACE>" + BRACE_PATTERN + ")"
                  + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                  + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                  + "|(?<STRING>" + STRING_PATTERN + ")"
                  + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
            ).toRegex()
   }
}