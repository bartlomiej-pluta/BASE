package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext

class MapCanvas : Renderable {
    private var layers: List<Layer>? = null
    private var rows: Int? = null
    private var columns: Int? = null
    private var tileWidth: Double? = null
    private var tileHeight: Double? = null
    private var mapWidth: Double? = null
    private var mapHeight: Double? = null

    fun updateMap(gameMap: GameMap) {
        layers = gameMap.layers
        rows = gameMap.rows
        columns = gameMap.columns
        tileWidth = gameMap.tileSet.tileWidth.toDouble()
        tileHeight = gameMap.tileSet.tileHeight.toDouble()
        mapWidth = gameMap.columns * gameMap.tileSet.tileWidth.toDouble()
        mapHeight = gameMap.rows * gameMap.tileSet.tileHeight.toDouble()
    }

    override fun render(gc: GraphicsContext) {
        layers?.forEach { dispatchLayerRender(gc, it) }
        renderGrid(gc)
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
}