package com.bartlomiejpluta.base.editor.code.view.editor

import tornadofx.Fragment

class CodeEditorFragment : Fragment() {
   private val editorView = find<CodeEditorView>()

   fun shutdown() {
      editorView.shutdown()
   }

   override val root = editorView.root
}