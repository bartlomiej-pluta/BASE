package com.bartlomiejpluta.base.editor.code.view

import tornadofx.Fragment

class CodeEditorFragment : Fragment() {
   private val editorView = find<CodeEditorView>()

   fun shutdown() {
      editorView.shutdown()
   }

   override val root = editorView.root
}