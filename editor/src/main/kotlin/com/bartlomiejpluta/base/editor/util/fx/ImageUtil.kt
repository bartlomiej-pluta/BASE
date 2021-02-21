package com.bartlomiejpluta.base.editor.util.fx

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object ImageUtil {
   fun scale(source: Image, targetWidth: Int, targetHeight: Int, preserveRatio: Boolean) = ImageView(source).apply {
      this.isSmooth = false
      this.isPreserveRatio = preserveRatio
      this.fitWidth = targetWidth.toDouble()
      this.fitHeight = targetHeight.toDouble()
   }.snapshot(null, null)

   fun save(image: Image, file: File) {
      val buffered = BufferedImage(image.width.toInt(), image.height.toInt(), BufferedImage.TYPE_INT_ARGB)
      val reader = image.pixelReader
      for (x in 0 until image.width.toInt()) {
         for (y in 0 until image.height.toInt()) {
            buffered.setRGB(x, y, reader.getArgb(x, y))
         }
      }

      ImageIO.write(buffered, file.extension, file)
   }
}