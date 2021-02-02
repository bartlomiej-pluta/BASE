package com.bartlomiejpluta.base.editor.model.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.model.map.tileset.Tile
import com.bartlomiejpluta.base.editor.model.map.tileset.TileSet
import com.bartlomiejpluta.base.editor.view.render.Renderable
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.canvas.GraphicsContext

class Grid(private val tileSet: TileSet, private val rows: Int, private val columns: Int) : Renderable {
    private var tileWidth = tileSet.tileWidth.toDouble()
    private var tileHeight = tileSet.tileHeight.toDouble()
    private var mapWidth = columns * tileWidth
    private var mapHeight = rows * tileHeight

    override fun render(gc: GraphicsContext) {
        gc.lineWidth = LINE_WIDTH

        for (row in 0 until rows) {
            gc.strokeLine(0.0, row * tileHeight, mapWidth, row * tileHeight)
        }

        for (column in 0 until columns) {
            gc.strokeLine(column * tileWidth, 0.0, column * tileWidth, mapHeight)
        }
    }

    companion object {
        const val LINE_WIDTH = 1.5
    }
}

class GameMap(private val tileSet: TileSet, private val rows: Int, private val columns: Int) : Renderable {
    val layers = mutableListOf<Layer>()
    private val grid = Grid(tileSet, rows, columns)

    val width = columns * tileSet.tileWidth

    val height = columns * tileSet.tileWidth

    fun createTileLayer(tile: Int) = createTileLayer().apply {
        val layerId = layers.size - 1
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                setTile(layerId, row, column, tile)
            }
        }
    }

    fun createTileLayer(tileRow: Int, tileColumn: Int) = createTileLayer().apply {
        val layerId = layers.size - 1
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                setTile(layerId, row, column, tileRow, tileColumn)
            }
        }
    }

    fun createTileLayer() = apply { layers.add(TileLayer(Array(rows) { Array(columns) { null } })) }

    fun setTile(layer: Int, row: Int, column: Int, tile: Int) = apply {
        (layers[layer] as TileLayer).setTile(row, column, tileSet.getTile(tile))
    }

    fun setTile(layer: Int, row: Int, column: Int, tileRow: Int, tileColumn: Int) = apply {
        (layers[layer] as TileLayer).setTile(row, column, tileSet.getTile(tileRow, tileColumn))
    }

    override fun render(gc: GraphicsContext) {
        layers.forEach { it.render(gc) }
        grid.render(gc)
    }
}