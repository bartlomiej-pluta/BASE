package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.autotile.model.AutoTile
import com.bartlomiejpluta.base.editor.map.model.enumeration.ImageLayerMode
import com.bartlomiejpluta.base.editor.map.model.layer.*
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment


class MapCanvas(val map: GameMapVM, private val editorStateVM: EditorStateVM, private val painter: MapPainter) :
   Renderable {
   private var tileWidth = map.tileWidth
   private var tileHeight = map.tileHeight

   private lateinit var grid: WritableImage
   private lateinit var background: WritableImage

   init {
      map.widthProperty.addListener { _, _, _ ->
         createGridImage()
         createBackgroundImage()
      }

      map.heightProperty.addListener { _, _, _ ->
         createGridImage()
         createBackgroundImage()
      }

      createGridImage()
      createBackgroundImage()
   }

   private fun createGridImage() {
      grid = WritableImage(map.width.toInt(), map.height.toInt())

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
      background = WritableImage(map.width.toInt(), map.height.toInt())

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
      if (editorStateVM.renderAllLayers) {
         renderOverlappingLayers(gc)
      }
      renderGrid(gc)
      painter.render(gc)
   }

   private fun renderSelectedLayer(gc: GraphicsContext) {
      map.layers.getOrNull(editorStateVM.selectedLayerIndex)?.let { dispatchLayerRender(gc, it) }
   }

   private fun renderCover(gc: GraphicsContext) {
      if (!editorStateVM.coverUnderlyingLayers) {
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

   private fun renderOverlappingLayers(gc: GraphicsContext) {
      for (layer in map.layers.drop(editorStateVM.selectedLayerIndex + 1)) {
         dispatchLayerRender(gc, layer)
      }
   }

   private fun dispatchLayerRender(gc: GraphicsContext, layer: Layer) {
      when (layer) {
         is TileLayer -> renderTileLayer(gc, layer)
         is AutoTileLayer -> renderAutoTileLayer(gc, layer)
         is ObjectLayer -> renderObjectLayer(gc, layer)
         is ColorLayer -> renderColorLayer(gc, layer)
         is ImageLayer -> renderImageLayer(gc, layer)
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

   private fun renderAutoTileLayer(gc: GraphicsContext, layer: AutoTileLayer) {
      for ((row, columns) in layer.layer.withIndex()) {
         for ((column, tile) in columns.withIndex()) {
            if(tile > 0) {
               renderAutoTile(gc, layer.autoTile, layer, column, row)
            }
         }
      }
   }

   private fun renderAutoTile(gc: GraphicsContext, autoTile: AutoTile, layer: AutoTileLayer, column: Int, row: Int) {
      val (topLeft, topRight, bottomLeft, bottomRight) = autoTile.getTile(layer, row, column, layer.connect)
      val x = column * tileWidth
      val y = row * tileHeight
      gc.drawImage(topLeft, x, y)
      gc.drawImage(topRight, x + tileWidth/2, y)
      gc.drawImage(bottomLeft, x, y + tileHeight/2)
      gc.drawImage(bottomRight, x + tileWidth/2, y + tileHeight/2)
   }

   private fun renderObjectLayer(gc: GraphicsContext, objectLayer: ObjectLayer) {
      if (editorStateVM.selectedLayer is ObjectLayer) {
         renderObjectPassageMap(gc, objectLayer)
      }

      renderObjects(gc, objectLayer)
      renderLabels(gc, objectLayer)
   }

   private fun renderObjectPassageMap(gc: GraphicsContext, objectLayer: ObjectLayer) {
      for ((row, columns) in objectLayer.passageMap.withIndex()) {
         for ((column, passage) in columns.withIndex()) {
            PassageAbilitySymbol.render(gc, column * tileWidth, row * tileHeight, tileWidth, tileHeight, passage)
         }
      }
   }

   private fun renderObjects(gc: GraphicsContext, objectLayer: ObjectLayer) {
      for (mapObject in objectLayer.objects) {
         val alpha = gc.globalAlpha
         val fill = gc.fill

         gc.globalAlpha = OBJECT_FILL_ALPHA
         gc.fill = OBJECT_COLOR

         gc.fillRect(
            mapObject.x * tileWidth + OBJECT_MARGIN,
            mapObject.y * tileHeight + OBJECT_MARGIN,
            tileWidth - 2 * OBJECT_MARGIN,
            tileHeight - 2 * OBJECT_MARGIN
         )

         gc.globalAlpha = 1.0
         gc.stroke = OBJECT_COLOR
         gc.strokeRect(
            mapObject.x * tileWidth + OBJECT_MARGIN,
            mapObject.y * tileHeight + OBJECT_MARGIN,
            tileWidth - 2 * OBJECT_MARGIN,
            tileHeight - 2 * OBJECT_MARGIN
         )

         gc.globalAlpha = alpha
         gc.fill = fill
      }
   }

   private fun renderLabels(gc: GraphicsContext, objectLayer: ObjectLayer) {
      val alpha = gc.globalAlpha
      val fill = gc.fill
      val width = gc.lineWidth
      val align = gc.textAlign
      val baseLine = gc.textBaseline
      val font = gc.font

//      gc.font = LABEL_FONT

      for (mapLabel in objectLayer.labels) {

//         gc.globalAlpha = OBJECT_FILL_ALPHA
//         gc.fill = OBJECT_COLOR
//
//         gc.fillRect(
//            mapObject.x * tileWidth + OBJECT_MARGIN,
//            mapObject.y * tileHeight + OBJECT_MARGIN,
//            tileWidth - 2 * OBJECT_MARGIN,
//            tileHeight - 2 * OBJECT_MARGIN
//         )

         gc.globalAlpha = 1.0
         gc.stroke = LABEL_COLOR
         gc.fill = LABEL_COLOR
         gc.lineWidth = LABEL_FONT_WIDTH

         gc.textAlign = TextAlignment.CENTER
         gc.textBaseline = VPos.CENTER

         gc.fillText(
            "${mapLabel.label[0]}.${mapLabel.label[mapLabel.label.lastIndex]}",
            mapLabel.x * tileWidth + tileWidth / 2,
            mapLabel.y * tileHeight + tileHeight / 2
         )

         gc.lineWidth = LABEL_WIDTH

         gc.strokeRect(
            mapLabel.x * tileWidth + LABEL_MARGIN + LABEL_WIDTH/2,
            mapLabel.y * tileHeight + LABEL_MARGIN + LABEL_WIDTH/2,
            tileWidth - 2 * LABEL_MARGIN - LABEL_WIDTH/2,
            tileHeight - 2 * LABEL_MARGIN - LABEL_WIDTH/2
         )
      }

      gc.globalAlpha = alpha
      gc.fill = fill
      gc.lineWidth = width
      gc.textAlign = align
      gc.textBaseline = baseLine
      gc.font = font
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

   private fun renderImageLayer(gc: GraphicsContext, imageLayer: ImageLayer) {
      val alpha = gc.globalAlpha
      gc.globalAlpha = imageLayer.opacity / 100.0

      val x = imageLayer.x.toDouble()
      val y = imageLayer.y.toDouble()

      when (imageLayer.mode) {
         ImageLayerMode.FIT_MAP -> gc.drawImage(imageLayer.image, x, y, map.width, map.height)
         else -> {
            val width = imageLayer.image.width * imageLayer.scaleX
            val height = imageLayer.image.height * imageLayer.scaleY
            gc.drawImage(imageLayer.image, x, y, width, height)
         }
      }

      gc.globalAlpha = alpha
   }

   private fun renderGrid(gc: GraphicsContext) {
      if (editorStateVM.showGrid) {
         gc.drawImage(grid, 0.0, 0.0)
      }
   }

   companion object {
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.8, 0.8, 0.8, 1.0)

      private val OBJECT_COLOR = Color.WHITE
      private val LABEL_COLOR = Color.CYAN
      private val LABEL_WIDTH = 2.0
      private val LABEL_FONT_WIDTH = 4.0
      private const val OBJECT_FILL_ALPHA = 0.3
      private const val OBJECT_MARGIN = 4
      private const val LABEL_MARGIN = 1
   }
}