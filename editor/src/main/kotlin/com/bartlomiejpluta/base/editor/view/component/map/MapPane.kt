package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.view.render.renderer.Renderer
import javafx.scene.canvas.Canvas

class MapPane(map: GameMap) : Canvas() {
    private val mapCanvas = MapCanvas(map)
    private val renderer = Renderer(graphicsContext2D, mapCanvas)

    init {
        width = map.width.toDouble()
        height = map.height.toDouble()
        renderer.start()
    }

    fun updateMap(map: GameMap) {
        mapCanvas.updateMap(map)
    }
}