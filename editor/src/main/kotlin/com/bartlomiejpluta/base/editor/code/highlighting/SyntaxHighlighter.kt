package com.bartlomiejpluta.base.editor.code.highlighting

import org.fxmisc.richtext.model.StyleSpans
import tornadofx.Stylesheet

interface SyntaxHighlighter {
   fun highlight(code: String): StyleSpans<Collection<String>>
   val stylesheet: Stylesheet
}