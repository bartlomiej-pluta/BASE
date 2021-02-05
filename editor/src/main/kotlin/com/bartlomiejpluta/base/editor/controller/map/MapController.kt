package com.bartlomiejpluta.base.editor.controller.map

import com.bartlomiejpluta.base.editor.command.service.UndoRedoService
import com.bartlomiejpluta.base.editor.controller.tileset.TileSetController
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import org.springframework.stereotype.Component
import tornadofx.Controller

@Component
class MapController : Controller() {
   private val undoRedoService: UndoRedoService by inject()
   private val tileSetController: TileSetController by inject()

   private val map1 = GameMap(tileSetController.tileset, 20, 20)

   private val map2 = GameMap(tileSetController.tileset, 50, 50)

   fun getMap(id: Int) = when (id) {
      1 -> map1
      else -> map2
   }
}