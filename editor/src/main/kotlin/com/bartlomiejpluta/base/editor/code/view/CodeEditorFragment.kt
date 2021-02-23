package com.bartlomiejpluta.base.editor.code.view

import tornadofx.Fragment

class CodeEditorFragment : Fragment() {
   private val editorView = find<CodeEditorView>()

   override val root = editorView.root
}