package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.map.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class MapPane(paintingCallback: (MapPaintingTrace) -> Unit) : Canvas(), EventHandler<MouseEvent> {
    private var tileSet: TileSet? = null
    private val mapCanvas = MapCanvas(paintingCallback)

    init {
        onMouseMoved = this
        onMouseDragged = this
        onMousePressed = this
        onMouseReleased = this
    }

    fun updateMap(map: GameMap) {
        tileSet = map.tileSet
        width = map.width.toDouble()
        height = map.height.toDouble()
        mapCanvas.updateMap(map)
        render()
    }

    fun render() {
        mapCanvas.render(graphicsContext2D)
    }

    override fun handle(event: MouseEvent?) {
        if (event != null && tileSet != null) {
            mapCanvas.handleMouseInput(MapMouseEvent.of(event, tileSet!!))
        }

        mapCanvas.render(graphicsContext2D)
    }
}