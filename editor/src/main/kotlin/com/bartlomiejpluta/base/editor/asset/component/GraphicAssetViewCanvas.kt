package com.bartlomiejpluta.base.editor.asset.component

import com.bartlomiejpluta.base.editor.asset.model.GraphicAsset
import com.bartlomiejpluta.base.editor.asset.viewmodel.GraphicAssetVM
import javafx.beans.property.IntegerProperty
import javafx.beans.property.Property
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.scene.text.TextAlignment
import tornadofx.getValue
import java.io.File

class GraphicAssetViewCanvas(val rowsProperty: IntegerProperty, val columnsProperty: IntegerProperty, val fileProperty: Property<File>) : Canvas() {
   private var image: Image? = null

   val rows by rowsProperty
   val columns by columnsProperty

   var chunkWidth: Int = 1
      private set

   var chunkHeight: Int = 1
      private set

   var singleChunk = rows == 1 && columns == 1
      private set

   init {
      rowsProperty.addListener { _, _, _ -> updateAsset() }
      columnsProperty.addListener { _, _, _ -> updateAsset() }
      fileProperty.addListener { _, _, _ -> updateAsset() }
      updateAsset()
   }

   constructor(asset: GraphicAssetVM) : this(asset.rowsProperty, asset.columnsProperty, asset.fileProperty)

   private fun updateAsset() {
      val file = fileProperty.value

      if (file != null) {
         image = Image(fileProperty.value.inputStream())
         width = image!!.width + OFFSET_X * 2
         height = image!!.height + OFFSET_Y * 2
         chunkWidth = (image!!.width / columns).toInt()
         chunkHeight = (image!!.height / rows).toInt()
         singleChunk = rows == 1 && columns == 1
      } else {
         image = null
         width = 0.0
         height = 0.0
         chunkWidth = 0
         chunkHeight = 0
         singleChunk = true
      }

      render()
   }

   fun render() {
      graphicsContext2D.clearRect(0.0, 0.0, width, height)

      if (image == null) {
         return
      }

      graphicsContext2D.drawImage(createBackgroundImage(), OFFSET_X, OFFSET_Y)
      graphicsContext2D.drawImage(image, OFFSET_X, OFFSET_Y)

      if (!singleChunk) {
         graphicsContext2D.textAlign = TextAlignment.LEFT
         for (i in 0 until columns) {
            graphicsContext2D.fillText(i.toString(), OFFSET_X + chunkWidth * i + INDEX_OFFSET_X, OFFSET_Y - 5.0)
         }

         graphicsContext2D.textAlign = TextAlignment.RIGHT
         for (i in 0 until rows) {
            graphicsContext2D.fillText(i.toString(), OFFSET_X - 5.0, OFFSET_Y + chunkHeight * i + INDEX_OFFSET_Y)
         }
      }

      if (!singleChunk) graphicsContext2D.drawImage(createGrid(), OFFSET_X, OFFSET_Y)
   }

   private fun createBackgroundImage(): WritableImage {
      val background = WritableImage(image!!.width.toInt(), image!!.height.toInt())

      val writer = background.pixelWriter
      for (x in 0 until background.width.toInt()) {
         for (y in 0 until background.height.toInt()) {
            val color = when (((x / BACKGROUND_TILE_WIDTH) + (y / BACKGROUND_TILE_HEIGHT)) % 2) {
               0 -> BACKGROUND_COLOR1
               else -> BACKGROUND_COLOR2
            }

            writer.setColor(x, y, color)
         }
      }

      return background
   }

   private fun createGrid(): WritableImage {
      val grid = WritableImage(image!!.width.toInt(), image!!.height.toInt())

      val writer = grid.pixelWriter
      val color = Color.BLACK
      for (x in 0 until grid.width.toInt()) {
         for (y in 0 until grid.height.toInt()) {
            if (x % chunkWidth == 0) {
               writer.setColor(x, y, color)
            }

            if (y % chunkHeight == 0) {
               writer.setColor(x, y, color)
            }
         }
      }

      val lastX = grid.width.toInt() - 1
      val lastY = grid.height.toInt() - 1

      for (x in 0 until grid.width.toInt()) {
         writer.setColor(x, 0, color)
         writer.setColor(x, lastY, color)
      }

      for (y in 0 until grid.height.toInt()) {
         writer.setColor(0, y, color)
         writer.setColor(lastX, y, color)
      }

      return grid
   }

   companion object {
      private const val BACKGROUND_TILE_WIDTH = 5
      private const val BACKGROUND_TILE_HEIGHT = 5
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.95, 0.95, 0.95, 1.0)
      private const val OFFSET_X = 30.0
      private const val OFFSET_Y = 30.0
      private const val INDEX_OFFSET_X = 0.0
      private const val INDEX_OFFSET_Y = 10.0
   }
}