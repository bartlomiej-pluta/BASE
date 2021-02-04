package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class MapPane : Canvas(), EventHandler<MouseEvent> {
    private val mapCanvas = MapCanvas()

    init {
        onMouseMoved = this
        onMouseClicked = this
        onMouseDragged = this
    }

    fun updateMap(map: GameMap) {
        width = map.width.toDouble()
        height = map.height.toDouble()
        mapCanvas.updateMap(map)
    }

    fun render() {
        mapCanvas.render(graphicsContext2D)
    }

    override fun handle(event: MouseEvent?) {
        mapCanvas.handle(event)
        mapCanvas.render(graphicsContext2D)
    }
}