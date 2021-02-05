package com.bartlomiejpluta.base.editor.view.component.map

import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapCanvas
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPainter
import com.bartlomiejpluta.base.editor.render.canvas.map.MapPaintingTrace
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent


class MapPane(map: GameMapVM, brushVM: BrushVM, paintingCallback: (MapPaintingTrace) -> Unit) : Canvas(), EventHandler<MouseEvent> {
    private var tileSet = map.tileSet
    private val painter = MapPainter(map, brushVM, paintingCallback)
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