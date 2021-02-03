package com.bartlomiejpluta.base.editor.model.map.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.model.map.tileset.TileSet


class GameMap(val tileSet: TileSet, val rows: Int, val columns: Int) {
    val layers = mutableListOf<Layer>()

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

    companion object {
        val EMPTY_8x8 = GameMap(TileSet.EMPTY, 8, 8)
    }
}