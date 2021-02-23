package com.bartlomiejpluta.base.editor.code.highlighting

import org.fxmisc.richtext.model.StyleSpans

interface SyntaxHighlighter {
   fun highlight(code: String): StyleSpans<Collection<String>>
}