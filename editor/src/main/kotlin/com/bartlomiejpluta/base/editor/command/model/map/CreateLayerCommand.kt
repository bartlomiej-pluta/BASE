package com.bartlomiejpluta.base.editor.command.model.map

import com.bartlomiejpluta.base.editor.command.model.base.Command
import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.map.GameMap

class CreateLayerCommand(private val map: GameMap, private val layer: Layer): Undoable, Command {

   override fun execute() {
      map.layers += layer
   }

   override fun undo() {
      map.layers -= layer
   }

   override fun redo() {
      execute()
   }

   override val commandName = "Create map layer"
}