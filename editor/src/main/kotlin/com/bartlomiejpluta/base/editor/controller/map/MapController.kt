package com.bartlomiejpluta.base.editor.controller.map

import com.bartlomiejpluta.base.editor.controller.tileset.TileSetController
import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import tornadofx.Controller

class MapController : Controller() {
    private val tileSetController: TileSetController by inject()

    private val map1 = GameMap(tileSetController.tileset, 20, 20)
        .createTileLayer(0)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)

    private val map2 = GameMap(tileSetController.tileset, 50, 50)
        .createTileLayer(3)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)
        .createTileLayer(3, 5)

    fun getMap(id: Int) = when(id) {
        1 -> map1
        else -> map2
    }
}