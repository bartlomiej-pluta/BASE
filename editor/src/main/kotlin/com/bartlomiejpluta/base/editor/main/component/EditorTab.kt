package com.bartlomiejpluta.base.editor.main.component

import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.input.KeyCombination.keyCombination
import javafx.scene.input.KeyEvent

class EditorTab<T : EditorFragment>(val fragment: T, graphic: Node) : Tab() {

   init {
      this.content = fragment.root
      this.graphic = graphic
   }

   fun handleShortcut(event: KeyEvent) = fragment.handleShortcut(event)

   companion object {
      val EXECUTE_SHORTCUT = keyCombination("Ctrl+Enter")!!
      val SAVE_SHORTCUT = keyCombination("Ctrl+S")!!
      val UNDO_SHORTCUT = keyCombination("Ctrl+Z")!!
      val REDO_SHORTCUT = keyCombination("Ctrl+Y")!!
   }
}