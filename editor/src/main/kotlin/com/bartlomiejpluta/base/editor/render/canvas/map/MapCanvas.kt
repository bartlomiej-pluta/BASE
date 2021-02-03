package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext

class MapCanvas(gameMap: GameMap) : Renderable {
    private var layers: List<Layer>
    private var rows: Int
    private var columns: Int
    private var tileWidth: Double
    private var tileHeight: Double
    private var mapWidth: Double
    private var mapHeight: Double

    init {
        layers = gameMap.layers
        rows = gameMap.rows
        columns = gameMap.columns
        tileWidth = gameMap.tileSet.tileWidth.toDouble()
        tileHeight = gameMap.tileSet.tileHeight.toDouble()
        mapWidth = columns * tileWidth
        mapHeight = rows * tileHeight
    }

    fun updateMap(gameMap: GameMap) {
        layers = gameMap.layers
        rows = gameMap.rows
        columns = gameMap.columns
        tileWidth = gameMap.tileSet.tileWidth.toDouble()
        tileHeight = gameMap.tileSet.tileHeight.toDouble()
        mapWidth = columns * tileWidth
        mapHeight = rows * tileHeight
    }

    override fun render(gc: GraphicsContext) {
        layers.forEach { dispatchLayerRender(gc, it) }
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

        for (row in 0 until rows) {
            gc.strokeLine(0.0, row * tileHeight, mapWidth, row * tileHeight)
        }

        for (column in 0 until columns) {
            gc.strokeLine(column * tileWidth, 0.0, column * tileWidth, mapHeight)
        }
    }
}