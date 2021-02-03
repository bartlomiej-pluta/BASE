package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.view.render.renderer.Renderer
import javafx.scene.canvas.Canvas

class MapPane : Canvas() {
    private val mapCanvas = MapCanvas()
    private val renderer = Renderer(graphicsContext2D, mapCanvas)

    init {
        renderer.start()
    }

    fun updateMap(map: GameMap) {
        width = map.width.toDouble()
        height = map.height.toDouble()
        mapCanvas.updateMap(map)
    }
}