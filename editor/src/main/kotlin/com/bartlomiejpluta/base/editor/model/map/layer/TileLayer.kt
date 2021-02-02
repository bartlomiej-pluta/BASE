package com.bartlomiejpluta.base.editor.model.map.layer

import com.bartlomiejpluta.base.editor.model.map.tileset.Tile
import javafx.scene.canvas.GraphicsContext

class TileLayer(private val layer: Array<Array<Tile?>>) : Layer {

    fun setTile(row: Int, column: Int, tile: Tile?) = apply { layer[row][column] = tile }

    override fun render(gc: GraphicsContext) {
        for ((row, columns) in layer.withIndex()) {
            for ((column, tile) in columns.withIndex()) {
                if (tile != null) {
                    gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
                }
            }
        }
    }
}