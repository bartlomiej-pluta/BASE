package com.bartlomiejpluta.base.editor.code.highlighting

import com.bartlomiejpluta.base.editor.code.stylesheet.PrologSyntaxHighlightingStylesheet
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import org.springframework.stereotype.Component

@Component
class PrologSyntaxHighlighter : SyntaxHighlighter {
   override fun highlight(code: String): StyleSpans<Collection<String>> = StyleSpansBuilder<Collection<String>>().let {
      val lastKeywordEnd = PATTERN.findAll(code).fold(0) { lastKeywordEnd, result ->
         val styleClass = when {
            result.groups["VARIABLE"] != null -> "variable"
            result.groups["PAREN"] != null -> "paren"
            result.groups["BRACE"] != null -> "brace"
            result.groups["BRACKET"] != null -> "bracket"
            result.groups["STRING"] != null -> "string"
            result.groups["NUMBER"] != null -> "number"
            result.groups["OPERATOR"] != null -> "operator"
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

   override val stylesheet = PrologSyntaxHighlightingStylesheet()

   companion object {
      private val VARIABLE_PATTERN = "([A-Z]\\w*)"
      private val PAREN_PATTERN = "\\(|\\)"
      private val BRACE_PATTERN = "\\{|\\}"
      private val BRACKET_PATTERN = "\\[|\\]"
      private val STRING_PATTERN = "((\"([^\"\\\\]|\\\\.)*\")|('([^'\\\\]|\\\\.)*'))"
      private val NUMBER_PATTERN = "\\b-?([0-9]*\\.[0-9]+|[0-9]+)\\w?\\b"
      private val OPERATOR_PATTERN = "[+-/*:|&?<>=!;]"
      private val COMMENT_PATTERN = """
      %[^
      ]*|/\*(.|\R)*?\*/
      """.trimIndent()

      private val PATTERN = (
         "(?<VARIABLE>$VARIABLE_PATTERN)"
            + "|(?<PAREN>$PAREN_PATTERN)"
            + "|(?<BRACE>$BRACE_PATTERN)"
            + "|(?<BRACKET>$BRACKET_PATTERN)"
            + "|(?<STRING>$STRING_PATTERN)"
            + "|(?<NUMBER>$NUMBER_PATTERN)"
            + "|(?<COMMENT>$COMMENT_PATTERN)"
            + "|(?<OPERATOR>$OPERATOR_PATTERN)"
         ).toRegex()
   }
}