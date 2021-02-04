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


class MapCanvas(private val paintingCallback: (MapPaintingTrace) -> Unit) : Renderable {
    private var map: GameMap? = null
    private var tileSet: TileSet? = null
    private var layers: List<Layer>? = null
    private var rows: Int? = null
    private var columns: Int? = null
    private var tileWidth: Double? = null
    private var tileHeight: Double? = null
    private var mapWidth: Double? = null
    private var mapHeight: Double? = null

    private var mouseColumn = -1
    private var mouseRow = -1

    private var currentTrace: MapPaintingTrace? = null

    fun updateMap(gameMap: GameMap) {
        map = gameMap
        tileSet = gameMap.tileSet
        layers = gameMap.layers
        rows = gameMap.rows
        columns = gameMap.columns
        tileWidth = gameMap.tileSet.tileWidth.toDouble()
        tileHeight = gameMap.tileSet.tileHeight.toDouble()
        mapWidth = gameMap.columns * gameMap.tileSet.tileWidth.toDouble()
        mapHeight = gameMap.rows * gameMap.tileSet.tileHeight.toDouble()
    }

    override fun render(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height);

        layers?.forEach { dispatchLayerRender(gc, it) }
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

        for (row in 0 until (rows ?: 0)) {
            gc.strokeLine(0.0, row * (tileHeight ?: 0.0), (mapWidth ?: 0.0), row * (tileHeight ?: 0.0))
        }

        for (column in 0 until (columns ?: 0)) {
            gc.strokeLine(column * (tileWidth ?: 0.0), 0.0, column * (tileWidth ?: 0.0), (mapHeight ?: 0.0))
        }
    }

    private fun renderCursor(gc: GraphicsContext) {
        if(mouseColumn >= 0 && mouseRow >= 0) {
            gc.fill = CURSOR_BRUSH
            gc.fillRect(mouseColumn * (tileWidth ?: 0.0), mouseRow * (tileHeight ?: 0.0), tileWidth ?: 0.0, tileHeight ?: 0.0)
            gc.fill = CURSOR_STROKE_BRUSH
            gc.strokeRect(mouseColumn * (tileWidth ?: 0.0), mouseRow * (tileHeight ?: 0.0), tileWidth ?: 0.0, tileHeight ?: 0.0)

        }
    }

    fun handleMouseInput(event: MapMouseEvent) {
        mouseRow = event.row
        mouseColumn = event.column

        val tile = tileSet?.getTile(24, 4)

        when(event.type) {
            MouseEvent.MOUSE_PRESSED -> map?.let {
                currentTrace = MapPaintingTrace(it, "Paint trace").apply {
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