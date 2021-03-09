package com.bartlomiejpluta.base.editor.main.component

import javafx.scene.Node
import javafx.scene.input.KeyEvent
import tornadofx.Fragment

abstract class EditorFragment(title: String? = null, icon: Node? = null) : Fragment(title, icon) {
   abstract fun handleShortcut(event: KeyEvent)
}