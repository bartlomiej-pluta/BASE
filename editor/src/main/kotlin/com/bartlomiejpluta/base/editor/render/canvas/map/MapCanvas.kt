package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.map.map.GameMap
import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import org.apache.commons.logging.LogFactory


class MapCanvas(val map: GameMap, var brush: MapBrush) : Renderable {
    var tileSet = map.tileSet
    private var layers = map.layers
    private var rows = map.rows
    private var columns = map.columns
    private var tileWidth = tileSet.tileWidth.toDouble()
    private var tileHeight = tileSet.tileHeight.toDouble()
    private var mapWidth = map.width.toDouble()
    private var mapHeight = map.height.toDouble()

    var mouseColumn = -1
        private set

    var mouseRow = -1
        private set

    //    private val brush = MapBrush(
//        this, arrayOf(
//            arrayOf(tileSet.getTile(140, 4), tileSet.getTile(140, 4), tileSet.getTile(140, 4)),
//            arrayOf(tileSet.getTile(140, 4), tileSet.getTile(140, 4), tileSet.getTile(140, 4)),
//            arrayOf(tileSet.getTile(140, 4), tileSet.getTile(140, 4), tileSet.getTile(140, 4))
//        )
//    )



    override fun render(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height);

        layers.forEach { dispatchLayerRender(gc, it) }
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

        for (row in 0..rows - 1) {
            gc.strokeLine(0.0, row * tileHeight, mapWidth, row * tileHeight)
        }

        for (column in 0 until columns) {
            gc.strokeLine(column * tileWidth, 0.0, column * tileWidth, mapHeight)
        }
    }

    private fun renderCursor(gc: GraphicsContext) {
//        if (mouseColumn >= 4 && mouseRow >= 4) {
//            gc.globalAlpha.let { alpha ->
//                gc.globalAlpha = 0.4
//                gc.drawImage(tile.image, mouseColumn * tileWidth, mouseRow * tileHeight)
//                gc.globalAlpha = alpha
//            }

            brush.render(gc)
//        }
    }

    fun handleMouseInput(event: MapMouseEvent) {
        mouseRow = event.row
        mouseColumn = event.column

//        when (event.type) {
//            MouseEvent.MOUSE_PRESSED -> {
//                currentTrace = MapPaintingTrace(map, "Paint trace").apply {
//                    paint(0, mouseRow, mouseColumn, tile)
//                }
//            }
//
//            MouseEvent.MOUSE_DRAGGED -> currentTrace?.apply {
//                paint(0, mouseRow, mouseColumn, tile)
//            }
//
//            MouseEvent.MOUSE_RELEASED -> currentTrace?.let {
//                paintingCallback(it)
//                currentTrace = null
//            }
//        }
    }

    companion object {
        private val log = LogFactory.getLog(MapCanvas::class.java)
    }
}