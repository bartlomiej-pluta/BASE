package com.bartlomiejpluta.base.editor.controller.map

import com.bartlomiejpluta.base.editor.controller.tileset.TileSetController
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import org.springframework.stereotype.Component
import tornadofx.Controller

@Component
class MapController : Controller() {
   private val tileSetController: TileSetController by inject()

   fun createMap(tileSet: TileSet, rows: Int, columns: Int) = GameMap(tileSet, rows, columns)
}