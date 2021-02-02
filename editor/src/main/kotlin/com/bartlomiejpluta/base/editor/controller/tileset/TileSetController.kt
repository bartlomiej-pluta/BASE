package com.bartlomiejpluta.base.editor.controller.tileset

import com.bartlomiejpluta.base.editor.model.map.tileset.TileSet
import tornadofx.Controller

class TileSetController : Controller() {
    val tileset = TileSet(resources.image("/textures/tileset.png"), 160, 8)
}