package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.layer.ImageLayer
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color


class MapCanvas(val map: GameMapVM, private val editorStateVM: EditorStateVM, private val painter: MapPainter) : Renderable {
   var tileSet = map.tileSet
   private var tileWidth = map.tileWidth
   private var tileHeight = map.tileHeight


   override fun render(gc: GraphicsContext) {
      gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

      renderBackground(gc)
      renderUnderlyingLayers(gc)
      renderCover(gc)
      renderSelectedLayer(gc)
      renderGrid(gc)
      painter.render(gc)
   }

   private fun renderSelectedLayer(gc: GraphicsContext) {
      map.layers.getOrNull(editorStateVM.selectedLayerIndex)?.let { dispatchLayerRender(gc, it) }
   }

   private fun renderCover(gc: GraphicsContext) {
      if(!editorStateVM.coverUnderlyingLayers) {
         return
      }

      gc.fill = Color.color(0.0, 0.0, 0.0, 0.4)
      gc.fillRect(0.0, 0.0, map.width, map.height)
   }

   private fun renderUnderlyingLayers(gc: GraphicsContext) {
      for (layer in map.layers.dropLast(if (editorStateVM.selectedLayerIndex < 0) 0 else map.layers.size - editorStateVM.selectedLayerIndex)) {
         dispatchLayerRender(gc, layer)
      }
   }

   private fun dispatchLayerRender(gc: GraphicsContext, layer: Layer) {
      when (layer) {
         is TileLayer -> renderTileLayer(gc, layer)
         is ObjectLayer -> renderObjectPassageMap(gc, layer)
         is ImageLayer -> renderImageLayer(gc, layer)
      }
   }

   private fun renderBackground(gc: GraphicsContext) {
      for (row in 0 until map.rows) {
         for (column in 0 until map.columns) {
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

   private fun renderObjectPassageMap(gc: GraphicsContext, objectLayer: ObjectLayer) {
      if (editorStateVM.selectedLayer !is ObjectLayer) {
         return
      }

      for ((row, columns) in objectLayer.passageMap.withIndex()) {
         for ((column, passage) in columns.withIndex()) {
            PassageAbilitySymbol.render(gc, column * tileWidth, row * tileHeight, tileWidth, tileHeight, passage)
         }
      }
   }

   private fun renderImageLayer(gc: GraphicsContext, imageLayer: ImageLayer) {
      val alpha = gc.globalAlpha
      val color = gc.fill

      gc.globalAlpha = imageLayer.alpha / 100.0
      gc.fill = Color.color(imageLayer.red / 100.0, imageLayer.green / 100.0, imageLayer.blue / 100.0)
      gc.fillRect(0.0, 0.0, map.width, map.height)

      gc.globalAlpha = alpha
      gc.fill = color
   }

   private fun renderGrid(gc: GraphicsContext) {
      if (!editorStateVM.showGrid) {
         return
      }

      val lineWidth = gc.lineWidth
      gc.lineWidth = 1.5
      gc.setLineDashes(0.7)

      gc.strokeLine(0.0, 0.0, map.width, 0.0)
      gc.strokeLine(0.0, 0.0, 0.0, map.height)
      gc.strokeLine(map.width, 0.0, map.width, map.height)
      gc.strokeLine(0.0, map.height, map.width, map.height)

      for (row in 0 until map.rows) {
         gc.strokeLine(0.0, row * tileHeight, map.width, row * tileHeight)
      }

      for (column in 0 until map.columns) {
         gc.strokeLine(column * tileWidth, 0.0, column * tileWidth, map.height)
      }

      gc.lineWidth = lineWidth
   }

   companion object {
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.8, 0.8, 0.8, 1.0)
   }
}