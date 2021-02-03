package com.bartlomiejpluta.base.editor.render.canvas.layer

import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext

class TileLayerCanvas(private val tileLayer: TileLayer) : Renderable {

    override fun render(gc: GraphicsContext) {
        for ((row, columns) in tileLayer.layer.withIndex()) {
            for ((column, tile) in columns.withIndex()) {
                if (tile != null) {
                    gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
                }
            }
        }
    }
}