package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import org.apache.commons.logging.LogFactory


class MapCanvas(private val map: GameMap, private val paintingCallback: (MapPaintingTrace) -> Unit) : Renderable {
    private var tileSet = map.tileSet
    private var layers = map.layers
    private var rows = map.rows
    private var columns = map.columns
    private var tileWidth = tileSet.tileWidth.toDouble()
    private var tileHeight = tileSet.tileHeight.toDouble()
    private var mapWidth = map.width.toDouble()
    private var mapHeight = map.height.toDouble()

    private var mouseColumn = -1
    private var mouseRow = -1

    private var currentTrace: MapPaintingTrace? = null


    override fun render(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height);

        layers.forEach { dispatchLayerRender(gc, it) }
        renderGrid(gc)
        renderCursor(gc)
    }

    private fun dispatchLayerRender(gc: GraphicsContext, layer: Layer) {
        when (layer) {
            is TileLayer -> renderTileLayer(gc, layer)
        }
    }

    private fun renderTileLayer(gc: GraphicsContext, tileLayer: TileLayer) {
        for ((row, columns) in tileLayer.layer.withIndex()) {
            for ((column, tile) in columns.withIndex()) {
                if (tile != null) {
                    gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
                }
            }
        }
    }

    private fun renderGrid(gc: GraphicsContext) {
        gc.lineWidth = 1.5

        for (row in 0..rows - 1) {
            gc.strokeLine(0.0, row * tileHeight, mapWidth, row * tileHeight)
        }

        for (column in 0 until columns) {
            gc.strokeLine(column * tileWidth, 0.0, column * tileWidth, mapHeight)
        }
    }

    private fun renderCursor(gc: GraphicsContext) {
        if (mouseColumn >= 0 && mouseRow >= 0) {
            gc.fill = CURSOR_BRUSH
            gc.fillRect(mouseColumn * tileWidth, mouseRow * tileHeight, tileWidth, tileHeight)

            gc.fill = CURSOR_STROKE_BRUSH
            gc.strokeRect(mouseColumn * tileWidth, mouseRow * tileHeight, tileWidth, tileHeight)
        }
    }

    fun handleMouseInput(event: MapMouseEvent) {
        mouseRow = event.row
        mouseColumn = event.column

        val tile = tileSet.getTile(24, 4)

        when (event.type) {
            MouseEvent.MOUSE_PRESSED -> {
                currentTrace = MapPaintingTrace(map, "Paint trace").apply {
                    paint(0, mouseRow, mouseColumn, tile)
                }
            }

            MouseEvent.MOUSE_DRAGGED -> currentTrace?.apply {
                paint(0, mouseRow, mouseColumn, tile)
            }

            MouseEvent.MOUSE_RELEASED -> currentTrace?.let {
                paintingCallback(it)
                currentTrace = null
            }
        }
    }

    companion object {
        val CURSOR_BRUSH = Color.color(0.7, 0.3, 0.8, 0.6)
        val CURSOR_STROKE_BRUSH = Color.color(0.7, 0.3, 0.8, 1.0)
        private val log = LogFactory.getLog(MapCanvas::class.java)
    }
}