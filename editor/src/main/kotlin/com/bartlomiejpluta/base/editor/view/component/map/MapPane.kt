package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.tileset.Tile
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.model.map.brush.Brush
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPainter
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent


class MapPane(map: GameMap, brush: Brush, paintingCallback: (MapPaintingTrace) -> Unit) : Canvas(), EventHandler<MouseEvent> {
    private var tileSet = map.tileSet
    private val painter = MapPainter(map, brush, paintingCallback)
    private val mapCanvas = MapCanvas(map, painter)

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
            painter.handleMouseInput(MapMouseEvent.of(event, tileSet))
        }

        mapCanvas.render(graphicsContext2D)
    }
}