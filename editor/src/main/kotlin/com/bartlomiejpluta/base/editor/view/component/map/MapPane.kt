package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.map.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class MapPane(map: GameMap, paintingCallback: (MapPaintingTrace) -> Unit) : Canvas(), EventHandler<MouseEvent> {
    private var tileSet = map.tileSet
    private val mapCanvas = MapCanvas(map, paintingCallback)

    init {
        onMouseMoved = this
        onMouseDragged = this
        onMousePressed = this
        onMouseReleased = this

        tileSet = map.tileSet
        width = map.width.toDouble()
        height = map.height.toDouble()
        render()
    }

    fun render() {
        mapCanvas.render(graphicsContext2D)
    }

    override fun handle(event: MouseEvent?) {
        if (event != null) {
            mapCanvas.handleMouseInput(MapMouseEvent.of(event, tileSet))
        }

        mapCanvas.render(graphicsContext2D)
    }
}