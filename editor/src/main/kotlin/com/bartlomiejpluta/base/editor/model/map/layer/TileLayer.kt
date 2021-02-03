package com.bartlomiejpluta.base.editor.model.map.layer

import com.bartlomiejpluta.base.editor.model.map.tileset.Tile
import javafx.scene.canvas.GraphicsContext

class TileLayer(val layer: Array<Array<Tile?>>) : Layer {

    fun setTile(row: Int, column: Int, tile: Tile?) = apply { layer[row][column] = tile }
}