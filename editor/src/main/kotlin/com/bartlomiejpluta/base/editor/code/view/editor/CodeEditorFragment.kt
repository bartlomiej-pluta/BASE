package com.bartlomiejpluta.base.editor.code.view.editor

import com.bartlomiejpluta.base.editor.main.component.EditorFragment
import com.bartlomiejpluta.base.editor.main.component.EditorTab.Companion.REDO_SHORTCUT
import com.bartlomiejpluta.base.editor.main.component.EditorTab.Companion.SAVE_SHORTCUT
import com.bartlomiejpluta.base.editor.main.component.EditorTab.Companion.UNDO_SHORTCUT
import javafx.scene.input.KeyEvent

class CodeEditorFragment : EditorFragment() {
   private val editorView = find<CodeEditorView>()

   fun shutdown() {
      editorView.shutdown()
   }

   override val root = editorView.root

   override fun handleShortcut(event: KeyEvent) {
      when {
         SAVE_SHORTCUT.match(event) -> {
            editorView.save()
            event.consume()
         }

         UNDO_SHORTCUT.match(event) -> {
            editorView.undo()
            event.consume()
         }

         REDO_SHORTCUT.match(event) -> {
            editorView.redo()
            event.consume()
         }
      }
   }
}