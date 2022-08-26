package com.bartlomiejpluta.base.editor.util

import javafx.scene.image.Image
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import java.nio.ByteBuffer

object ImageUtil {
   fun cropImage(image: Image, x: Int, y: Int, w: Int, h: Int): Image {
      val reader = image.pixelReader
      val buffer = ByteBuffer.allocate(w * h * 4)
      reader.getPixels(x, y, w, h, PixelFormat.getByteBgraInstance(), buffer, 4 * w)
      val output = WritableImage(w, h)
      val writer = output.pixelWriter
      writer.setPixels(0, 0, w, h, PixelFormat.getByteBgraInstance(), buffer, 4 * w)

      return output
   }
}