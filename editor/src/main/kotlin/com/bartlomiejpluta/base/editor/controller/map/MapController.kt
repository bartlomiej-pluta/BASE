package com.bartlomiejpluta.base.editor.controller.map

import com.bartlomiejpluta.base.editor.controller.tileset.TileSetController
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import tornadofx.Controller

class MapController : Controller() {
    private val tileSetController: TileSetController by inject()

    val map = GameMap(tileSetController.tileset, 20, 20)
        .createTileLayer(0)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
}