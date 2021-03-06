package com.bartlomiejpluta.base.editor.command.model.map

import com.bartlomiejpluta.base.editor.command.model.base.Command
import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.map.GameMap

class RemoveLayerCommand(private val map: GameMap, private val layerIndex: Int) : Undoable, Command {
   private var layer: Layer? = null

   override fun execute() {
      layer = map.layers.removeAt(layerIndex)
   }

   override fun undo() {
      map.layers.add(layerIndex, layer)
      layer = null
   }

   override fun redo() {
      execute()
   }

   override val commandName = "Remove layer"
}