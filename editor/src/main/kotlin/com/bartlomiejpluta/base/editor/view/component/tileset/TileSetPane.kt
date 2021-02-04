package com.bartlomiejpluta.base.editor.view.component.tileset

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.tileset.TileSetCanvas
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.input.MouseEvent

class TileSetPane(private val tileSet: TileSet, selectionCallback: (Array<Array<Tile>>) -> Unit) : Canvas(), EventHandler<MouseEvent> {
    private val tileSetCanvas = TileSetCanvas(tileSet, selectionCallback)

    init {
        onMouseMoved = this
        onMouseDragged = this
        onMousePressed = this
        onMouseReleased = this

        width = tileSet.width.toDouble()
        height = tileSet.height.toDouble()

        render()
    }

    fun render() {
        tileSetCanvas.render(graphicsContext2D)
    }

    override fun handle(event: MouseEvent?) {
        if (event != null) {
            tileSetCanvas.handleMouseInput(MapMouseEvent.of(event, tileSet))
        }

        tileSetCanvas.render(graphicsContext2D)
    }
}