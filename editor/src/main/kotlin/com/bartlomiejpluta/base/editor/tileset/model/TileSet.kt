package com.bartlomiejpluta.base.editor.tileset.model

import com.bartlomiejpluta.base.editor.map.model.brush.Brush
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import tornadofx.getValue
import tornadofx.toObservable
import java.nio.ByteBuffer


class TileSet(name: String, image: Image, rows: Int, columns: Int) {
   val nameProperty = SimpleStringProperty(name)
   val name by nameProperty

   val imageProperty = SimpleObjectProperty(image)
   val image by imageProperty

   val rowsProperty = SimpleIntegerProperty(rows)
   val rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(columns)
   val columns by columnsProperty

   val tileWidthProperty = SimpleIntegerProperty(image.width.toInt() / columns)
   val tileWidth by tileWidthProperty

   val tileHeightProperty = SimpleIntegerProperty(image.height.toInt() / rows)
   val tileHeight by tileHeightProperty

   val widthProperty = SimpleIntegerProperty(tileWidth * columns)
   val width by widthProperty

   val heightProperty = SimpleIntegerProperty(tileHeight * rows)
   val height by heightProperty

   val tiles = (0 until rows * columns).map { cropTile(it / columns, it % columns) }.toObservable()

   val baseBrush: Brush
      get() = Brush.of(arrayOf(arrayOf(tiles[0])))

   private fun cropTile(row: Int, column: Int): Tile {
      val reader = image.pixelReader
      val buffer = ByteBuffer.allocate(tileWidth * tileHeight * 4)
      reader.getPixels(
         column * tileWidth,
         row * tileHeight,
         tileWidth,
         tileHeight,
         PixelFormat.getByteBgraInstance(),
         buffer,
         4 * tileWidth
      )
      val tile = WritableImage(tileWidth, tileHeight)
      val writer = tile.pixelWriter
      writer.setPixels(0, 0, tileWidth, tileHeight, PixelFormat.getByteBgraInstance(), buffer, 4 * tileWidth)

      return Tile(this, row, column, tile)
   }

   fun getTile(row: Int, column: Int) = tiles[(row.coerceIn(0 until rows)) * columns + column.coerceIn(0 until columns)]

   fun getTile(id: Int) = tiles[id]

   fun forEach(consumer: (row: Int, column: Int, tile: Tile) -> Unit) = tiles.forEachIndexed { id, tile ->
      consumer(id / columns, id % columns, tile)
   }
}