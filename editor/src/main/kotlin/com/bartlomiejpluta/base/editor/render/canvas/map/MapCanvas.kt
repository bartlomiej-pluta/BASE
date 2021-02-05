package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.map.layer.TileLayer
import com.bartlomiejpluta.base.editor.render.model.Renderable
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color


class MapCanvas(val map: GameMapVM, private val painter: MapPainter) : Renderable {
   var tileSet = map.tileSet
   private var layers = map.layers
   private var rows = map.rows
   private var columns = map.columns
   private var tileWidth = tileSet.tileWidth.toDouble()
   private var tileHeight = tileSet.tileHeight.toDouble()
   private var mapWidth = map.width.toDouble()
   private var mapHeight = map.height.toDouble()


   override fun render(gc: GraphicsContext) {
      gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

       renderBackground(gc)

      layers.forEach { dispatchLayerRender(gc, it) }

      renderGrid(gc)

      painter.render(gc)
   }

   private fun dispatchLayerRender(gc: GraphicsContext, layer: Layer) {
      when (layer) {
         is TileLayer -> renderTileLayer(gc, layer)
      }
   }

   private fun renderBackground(gc: GraphicsContext) {
      for (row in 0 until rows) {
         for (column in 0 until columns) {
            gc.fill = if ((row + column) % 2 == 0) BACKGROUND_COLOR1 else BACKGROUND_COLOR2
            gc.fillRect(column * tileWidth, row * tileHeight, tileWidth, tileHeight)
         }
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

      gc.strokeLine(0.0, 0.0, mapWidth, 0.0)
      gc.strokeLine(0.0, 0.0, 0.0, mapHeight)
      gc.strokeLine(mapWidth, 0.0, mapWidth, mapHeight)
      gc.strokeLine(0.0, mapHeight, mapWidth, mapHeight)

      for (row in 0 until rows) {
         gc.strokeLine(0.0, row * tileHeight, mapWidth, row * tileHeight)
      }

      for (column in 0 until columns) {
         gc.strokeLine(column * tileWidth, 0.0, column * tileWidth, mapHeight)
      }
   }

   companion object {
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.95, 0.95, 0.95, 0.95)
   }
}