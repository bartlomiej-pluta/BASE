package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.layer.ColorLayer
import com.bartlomiejpluta.base.editor.map.model.layer.Layer
import com.bartlomiejpluta.base.editor.map.model.layer.ObjectLayer
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color


class MapCanvas(val map: GameMapVM, private val editorStateVM: EditorStateVM, private val painter: MapPainter) : Renderable {
   var tileSet = map.tileSet
   private var tileWidth = map.tileWidth
   private var tileHeight = map.tileHeight

   private val grid = WritableImage(map.width.toInt(), map.height.toInt())
   private val background = WritableImage(map.width.toInt(), map.height.toInt())

   init {
      createGridImage()
      createBackgroundImage()
   }

   private fun createGridImage() {
      val writer = grid.pixelWriter
      val color = Color.BLACK
      for (x in 0 until map.width.toInt()) {
         for (y in 0 until map.height.toInt()) {
            if (x % tileWidth.toInt() == 0) {
               writer.setColor(x, y, color)
            }

            if (y % tileHeight.toInt() == 0) {
               writer.setColor(x, y, color)
            }
         }
      }
   }

   private fun createBackgroundImage() {
      val writer = background.pixelWriter

      for (x in 0 until map.width.toInt()) {
         for (y in 0 until map.height.toInt()) {
            val color = when (((x / tileWidth.toInt()) + (y / tileHeight.toInt())) % 2) {
               0 -> BACKGROUND_COLOR1
               else -> BACKGROUND_COLOR2
            }

            writer.setColor(x, y, color)
         }
      }
   }

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
         is ColorLayer -> renderColorLayer(gc, layer)
      }
   }

   private fun renderBackground(gc: GraphicsContext) {
      gc.drawImage(background, 0.0, 0.0)
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

   private fun renderColorLayer(gc: GraphicsContext, colorLayer: ColorLayer) {
      val alpha = gc.globalAlpha
      val color = gc.fill

      gc.globalAlpha = colorLayer.alpha / 100.0
      gc.fill = Color.color(colorLayer.red / 100.0, colorLayer.green / 100.0, colorLayer.blue / 100.0)
      gc.fillRect(0.0, 0.0, map.width, map.height)

      gc.globalAlpha = alpha
      gc.fill = color
   }

   private fun renderGrid(gc: GraphicsContext) {
      if (editorStateVM.showGrid) {
         gc.drawImage(grid, 0.0, 0.0)
      }
   }

   companion object {
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.8, 0.8, 0.8, 1.0)
   }
}