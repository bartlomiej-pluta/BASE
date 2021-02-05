package com.bartlomiejpluta.base.editor.model.map.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import tornadofx.observableListOf


class GameMap(val tileSet: TileSet, val rows: Int, val columns: Int) {
    val layers = observableListOf<Layer>()

    val width = columns * tileSet.tileWidth

    val height = columns * tileSet.tileWidth

    fun createTileLayer(name: String, tile: Int) = createTileLayer(name).apply {
        val layerId = layers.size - 1
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                setTile(layerId, row, column, tile)
            }
        }
    }

    fun createTileLayer(name: String, tileRow: Int, tileColumn: Int) = createTileLayer(name).apply {
        val layerId = layers.size - 1
        for (row in 0 until rows) {
            for (column in 0 until columns) {
                setTile(layerId, row, column, tileRow, tileColumn)
            }
        }
    }

    fun createTileLayer(name: String) = apply { layers.add(TileLayer(name, Array(rows) { Array(columns) { null } })) }

    fun setTile(layer: Int, row: Int, column: Int, tile: Int) = apply {
        (layers[layer] as TileLayer).setTile(row, column, tileSet.getTile(tile))
    }

    fun setTile(layer: Int, row: Int, column: Int, tileRow: Int, tileColumn: Int) = apply {
        (layers[layer] as TileLayer).setTile(row, column, tileSet.getTile(tileRow, tileColumn))
    }
}
