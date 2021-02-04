package com.bartlomiejpluta.base.editor.render.canvas.tileset

import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class TileSetCanvas(private val tileSet: TileSet) : Renderable, MapMouseEventHandler {
    private val tiles = tileSet.tiles
    private var selection = TileSetSelection(tileSet)

    private var mouseRow = -1
    private var mouseColumn = -1

    override fun render(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height);

        renderTiles(gc)
        selection.render(gc)
    }

    private fun renderTiles(gc: GraphicsContext) {
        for ((row, columns) in tiles.withIndex()) {
            for ((column, tile) in columns.withIndex()) {
                gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
            }
        }
    }

    override fun handleMouseInput(event: MapMouseEvent) {
        mouseRow = event.row
        mouseColumn = event.column

        when (event.type) {
            MouseEvent.MOUSE_PRESSED -> beginSelection(event)
            MouseEvent.MOUSE_DRAGGED -> proceedSelection(event)
        }
    }

    private fun beginSelection(event: MapMouseEvent) {
        if(event.button == MouseButton.PRIMARY) {
            selection.begin(event.row.toDouble(), event.column.toDouble())
        }
    }

    private fun proceedSelection(event: MapMouseEvent) {
        if(event.button == MouseButton.PRIMARY) {
            selection.finish(event.row.toDouble(), event.column.toDouble())
        }
    }
}