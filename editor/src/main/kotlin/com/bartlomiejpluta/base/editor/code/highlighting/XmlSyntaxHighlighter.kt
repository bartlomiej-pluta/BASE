package com.bartlomiejpluta.base.editor.code.highlighting

import com.bartlomiejpluta.base.editor.code.stylesheet.XmlSyntaxHighlightingStylesheet
import org.fxmisc.richtext.model.StyleSpansBuilder
import org.springframework.stereotype.Component


@Component
class XmlSyntaxHighlighter : SyntaxHighlighter {
   override fun highlight(code: String) = StyleSpansBuilder<Collection<String>>().apply {
      var last = 0;

      XML_TAG.findAll(code).forEach { matcher ->
         add(emptyList(), matcher.start - last);

         when {
            matcher.groups["PROLOG"] != null -> {
               add(listOf("prolog"), matcher.end - matcher.start)
            }

            matcher.groups["COMMENT"] != null -> {
               add(listOf("comment"), matcher.end - matcher.start)
            }

            matcher.groups["ELEMENT"] != null -> {
               add(listOf("tagmark"), matcher.end("OPEN") - matcher.start("OPEN"))
               add(listOf("tagname"), matcher.end("ELEM") - matcher.end("OPEN"))

               val attributesString = matcher.groups["ATTRS"]?.let(MatchGroup::value)?.takeIf(String::isNotEmpty)
               val attributesStringLength = attributesString?.length ?: 0

               if (attributesString?.isNotEmpty() == true) {
                  last = 0
                  ATTRIBUTES.findAll(attributesString).forEach { attr ->
                     add(emptyList(), attr.range.first - last)
                     add(listOf("attribute"), attr.end("ATTR") - attr.start("ATTR"))
                     add(listOf("tagmark"), attr.end("EQ") - attr.end("ATTR"))
                     add(listOf("value"), attr.end("VALUE") - attr.end("EQ"))
                     last = attr.end
                  }

                  if (attributesStringLength > last) {
                     add(emptyList(), attributesStringLength - last)
                  }
               }

               last = matcher.end("ATTRS")
               add(listOf("tagmark"), matcher.end("CLOSE") - last)
            }
         }

         last = matcher.end
      }

      add(emptyList(), code.length - last)
   }.create()

   private val MatchResult.start: Int
      get() = this.range.first

   private val MatchResult.end: Int
      get() = this.range.last + 1

   private fun MatchResult.start(group: String) = this.groups[group]?.range?.first ?: 0

   private fun MatchResult.end(group: String) = (this.groups[group]?.range?.last ?: 0) + 1

   override val stylesheet = XmlSyntaxHighlightingStylesheet()

   companion object {
      private val XML_TAG =
         """(?<ELEMENT>(?<OPEN></?\h*)(?<ELEM>[:.\-\w]+)(?<ATTRS>[^<>]*)(?<CLOSE>\h*/?>))|(?<COMMENT><!--[^<>]+-->)|(?<PROLOG><\?[^<>?]+?\?>)""".toRegex()

      private val ATTRIBUTES = """(?<ATTR>[:\w]+\h*)(?<EQ>=)(?<VALUE>\h*"[^"]+")""".toRegex()
   }
}

