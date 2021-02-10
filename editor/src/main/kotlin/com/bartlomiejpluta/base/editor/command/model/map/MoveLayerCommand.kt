package com.bartlomiejpluta.base.editor.command.model.map

import com.bartlomiejpluta.base.editor.command.model.base.Command
import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import tornadofx.swap

class MoveLayerCommand(private val map: GameMap, private val currentIndex: Int, private val newIndex: Int) : Undoable, Command {
   override fun execute() {
      map.layers.swap(currentIndex, newIndex)
   }

   override fun undo() {
      map.layers.swap(newIndex, currentIndex)
   }

   override fun redo() {
      execute()
   }

   override val commandName = "Move layer"
}