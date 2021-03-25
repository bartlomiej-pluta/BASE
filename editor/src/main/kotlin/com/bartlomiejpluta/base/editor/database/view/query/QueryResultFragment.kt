package com.bartlomiejpluta.base.editor.database.view.query

import com.bartlomiejpluta.base.editor.main.component.EditorFragment
import javafx.scene.input.KeyEvent

class QueryResultFragment : EditorFragment() {
   private val editorView = find<QueryResultView>()

   override val root = editorView.root

   override fun handleShortcut(event: KeyEvent) {

   }
}