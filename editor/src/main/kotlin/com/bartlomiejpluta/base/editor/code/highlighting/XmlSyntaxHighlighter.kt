package com.bartlomiejpluta.base.editor.code.highlighting

import com.bartlomiejpluta.base.editor.code.stylesheet.XmlSyntaxHighlightingStylesheet
import org.fxmisc.richtext.model.StyleSpansBuilder
import org.springframework.stereotype.Component


@Component
class XmlSyntaxHighlighter : SyntaxHighlighter {
   private val XML_TAG =
      """(?<ELEMENT>(?<OPEN></?\h*)(?<ELEM>[\w:]+)(?<ATTRS>[^<>]*)(?<CLOSE>\h*/?>))|(?<COMMENT><!--[^<>]+-->)|(?<PROLOG><\?[^<>?]+?\?>)""".toRegex()

   private val ATTRIBUTES = """(?<ATTR>[\w:]+\h*)(?<EQ>=)(?<VALUE>\h*"[^"]+")""".toRegex()

   override fun highlight(code: String) = StyleSpansBuilder<Collection<String>>().let {
      var lastKwEnd = 0;

      XML_TAG.findAll(code).forEach { matcher ->
         it.add(emptyList(), matcher.start - lastKwEnd);

         when {
            matcher.groups["PROLOG"] != null -> {
               it.add(listOf("prolog"), matcher.end - matcher.start)
            }

            matcher.groups["COMMENT"] != null -> {
               it.add(listOf("comment"), matcher.end - matcher.start)
            }

            matcher.groups["ELEMENT"] != null -> {
               it.add(listOf("tagmark"), matcher.end("OPEN") - matcher.start("OPEN"))
               it.add(listOf("anytag"), matcher.end("ELEM") - matcher.end("OPEN"))

               val attributesString = matcher.groups["ATTRS"]?.let(MatchGroup::value)?.takeIf(String::isNotEmpty)
               val attributesStringLength = attributesString?.length ?: 0

               if (attributesString?.isNotEmpty() == true) {
                  lastKwEnd = 0
                  ATTRIBUTES.findAll(attributesString).forEach { attr ->
                     it.add(emptyList(), attr.range.first - lastKwEnd)
                     it.add(listOf("attribute"), attr.end("ATTR") - attr.start("ATTR"))
                     it.add(listOf("tagmark"), attr.end("EQ") - attr.end("ATTR"))
                     it.add(listOf("avalue"), attr.end("VALUE") - attr.end("EQ"))
                     lastKwEnd = attr.end
                  }

                  if (attributesStringLength > lastKwEnd) {
                     it.add(emptyList(), attributesStringLength - lastKwEnd)
                  }
               }

               lastKwEnd = matcher.end("ATTRS")
               it.add(listOf("tagmark"), matcher.end("CLOSE") - lastKwEnd)
            }
         }

         lastKwEnd = matcher.end
      }

      it.add(emptyList(), code.length - lastKwEnd)

      it.create()
   }

   private val MatchResult.start: Int
      get() = this.range.first

   private val MatchResult.end: Int
      get() = this.range.last + 1

   private fun MatchResult.start(group: Int) = this.groups[group]?.range?.first ?: 0
   private fun MatchResult.start(group: String) = this.groups[group]?.range?.first ?: 0

   private fun MatchResult.end(group: Int) = (this.groups[group]?.range?.last ?: 0) + 1
   private fun MatchResult.end(group: String) = (this.groups[group]?.range?.last ?: 0) + 1

   override val stylesheet = XmlSyntaxHighlightingStylesheet()
}

