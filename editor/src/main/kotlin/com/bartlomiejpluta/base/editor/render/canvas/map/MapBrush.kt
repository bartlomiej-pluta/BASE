package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.tileset.Tile
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

class MapBrush(
    private val map: GameMap,
    private val brush: Array<Array<Tile>>,
    private val paintingCallback: (MapPaintingTrace) -> Unit
) : Renderable, MapMouseEventHandler {
    private val tileWidth = map.tileSet.tileWidth.toDouble()
    private val tileHeight = map.tileSet.tileHeight.toDouble()
    private val centerRow: Int
    private val centerColumn: Int

    private var mouseRow = -1
    private var mouseColumn = -1

    private var currentTrace: MapPaintingTrace? = null

    init {
        if (brush.isEmpty() || brush[0].isEmpty()) {
            throw IllegalStateException("Brush size must be at least 1x1")
        }

        centerRow = brush.size / 2
        centerColumn = brush[0].size / 2
    }

    override fun render(gc: GraphicsContext) {
        val alpha = gc.globalAlpha
        gc.globalAlpha = 0.4

        for ((row, columns) in brush.withIndex()) {
            for ((column, tile) in columns.withIndex()) {
                renderTile(gc, tile, column, row)
            }
        }

        gc.globalAlpha = alpha

    }

    private fun renderTile(gc: GraphicsContext, tile: Tile, column: Int, row: Int) {
        gc.drawImage(
            tile.image,
            tileWidth * (mouseColumn - centerColumn + column),
            tileHeight * (mouseRow - centerRow + row)
        )
    }

    override fun handleMouseInput(event: MapMouseEvent) {
        mouseRow = event.row
        mouseColumn = event.column

        when (event.type) {
            MouseEvent.MOUSE_PRESSED -> beginTrace(event)
            MouseEvent.MOUSE_DRAGGED -> proceedTrace()
            MouseEvent.MOUSE_RELEASED -> commitTrace(event)
        }
    }

    private fun beginTrace(event: MapMouseEvent) {
        if (event.button == MouseButton.PRIMARY) {
            currentTrace = MapPaintingTrace(map, "Paint trace").apply {
                for ((row, columns) in brush.withIndex()) {
                    for ((column, tile) in columns.withIndex()) {
                        paint(0, mouseRow - centerRow + row, mouseColumn - centerColumn + column, tile)
                    }
                }
            }
        }
    }

    private fun proceedTrace() {
        currentTrace?.apply {
            for ((row, columns) in brush.withIndex()) {
                for ((column, tile) in columns.withIndex()) {
                    paint(0, mouseRow - centerRow + row, mouseColumn - centerColumn + column, tile)
                }
            }
        }
    }

    private fun commitTrace(event: MapMouseEvent) {
        if (event.button == MouseButton.PRIMARY) {
            currentTrace?.let {
                paintingCallback(it)
                currentTrace = null
            }
        }
    }
}