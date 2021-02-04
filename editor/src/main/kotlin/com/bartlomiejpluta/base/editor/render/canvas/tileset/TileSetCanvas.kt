package com.bartlomiejpluta.base.editor.render.canvas.tileset

import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import com.bartlomiejpluta.base.editor.render.canvas.map.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.map.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext

class TileSetCanvas(private val tileSet: TileSet) : Renderable, MapMouseEventHandler {
    private val tiles = tileSet.tiles

    override fun render(gc: GraphicsContext) {
        renderTiles(gc)
    }

    private fun renderTiles(gc: GraphicsContext) {
        for ((row, columns) in tiles.withIndex()) {
            for ((column, tile) in columns.withIndex()) {
                gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
            }
        }
    }

    override fun handleMouseInput(event: MapMouseEvent) {

    }
}