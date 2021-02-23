package com.bartlomiejpluta.base.editor.main.model

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import tornadofx.*

class StructureCategory(name: String = "", var items: ObservableList<out Any> = observableListOf()) {
   val nameProperty = name.toProperty()
   val name by nameProperty
   val menu = ContextMenu()

   fun menuitem(text: String, graphic: Node? = null, action: () -> Unit) {
      MenuItem(text, graphic).apply {
         action { action() }
         menu.items.add(this)
      }
   }
}