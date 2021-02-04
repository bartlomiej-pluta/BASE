package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.render.canvas.map.MapBrush
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.map.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent



class MapPane(map: GameMap, paintingCallback: (MapPaintingTrace) -> Unit) : Canvas(), EventHandler<MouseEvent> {
    private var tileSet = map.tileSet
    private var brush: MapBrush
    private val mapCanvas: MapCanvas

    private val brushDefinition = arrayOf(
        arrayOf(tileSet.getTile(140, 4), tileSet.getTile(140, 5), tileSet.getTile(140, 6)),
        arrayOf(tileSet.getTile(141, 4), tileSet.getTile(141, 5), tileSet.getTile(141, 6)),
        arrayOf(tileSet.getTile(142, 4), tileSet.getTile(142, 5), tileSet.getTile(142, 6))
    )

    init {
        onMouseMoved = this
        onMouseDragged = this
        onMousePressed = this
        onMouseReleased = this

        brush = MapBrush(map, brushDefinition, paintingCallback)
        mapCanvas = MapCanvas(map, brush)

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
            brush.handleMouseInput(MapMouseEvent.of(event, tileSet))
        }

        mapCanvas.render(graphicsContext2D)
    }
}