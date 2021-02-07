package com.bartlomiejpluta.base.editor.tileset.model

import javafx.scene.image.Image

import javafx.scene.image.PixelReader

import javafx.scene.image.WritableImage


class Tile(
   tileSet: TileSet,
   row: Int,
   column: Int,
   val image: Image,
) {
   val id = row * tileSet.columns + column

   private fun cloneImage(image: Image): Image {
      val height = image.height.toInt()
      val width = image.width.toInt()
      val pixelReader = image.pixelReader
      val writableImage = WritableImage(width, height)
      val pixelWriter = writableImage.pixelWriter

      for (y in 0 until height) {
         for (x in 0 until width) {
            val color = pixelReader.getColor(x, y)
            pixelWriter.setColor(x, y, color)
         }
      }
      return writableImage
   }

   fun scale(image: Image, factor: Int): Image {
      val width = image.width.toInt()
      val height = image.height.toInt()
      val output = WritableImage(width * factor, height * factor)

      val reader: PixelReader = image.pixelReader
      val writer = output.pixelWriter

      for (y in 0 until height) {
         for (x in 0 until width) {
            val argb = reader.getArgb(x, y)
            for (dy in 0 until factor) {
               for (dx in 0 until factor) {
                  writer.setArgb(x * factor + dx, y * factor + dy, argb)
               }
            }
         }
      }

      return output
   }
}