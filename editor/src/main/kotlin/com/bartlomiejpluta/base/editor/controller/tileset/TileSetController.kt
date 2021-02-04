package com.bartlomiejpluta.base.editor.controller.tileset

import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import org.springframework.stereotype.Component
import tornadofx.Controller

@Component
class TileSetController : Controller() {
    val tileset = TileSet(resources.image("/textures/tileset.png"), 160, 8)
}