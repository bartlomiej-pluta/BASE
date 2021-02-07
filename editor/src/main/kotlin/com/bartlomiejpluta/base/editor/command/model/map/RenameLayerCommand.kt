package com.bartlomiejpluta.base.editor.command.model.map

import com.bartlomiejpluta.base.editor.command.model.base.Command
import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import com.bartlomiejpluta.base.editor.map.model.layer.Layer

class RenameLayerCommand(private val layer: Layer, private val newName: String) : Undoable, Command {
   private val formerName = layer.name

   override fun execute() {
      layer.name = newName
   }

   override fun undo() {
      layer.name = formerName
   }

   override fun redo() {
      execute()
   }

   override val commandName = "Rename layer"
}