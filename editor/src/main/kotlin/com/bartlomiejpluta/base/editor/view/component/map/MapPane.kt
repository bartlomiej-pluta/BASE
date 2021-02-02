package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.GameMap
import com.bartlomiejpluta.base.editor.view.render.Renderer
import javafx.scene.canvas.Canvas

class MapPane(map: GameMap) : Canvas() {
    private val renderer = Renderer(graphicsContext2D, map)

    init {
        width = map.width.toDouble()
        height = map.height.toDouble()
        renderer.start()
    }
}