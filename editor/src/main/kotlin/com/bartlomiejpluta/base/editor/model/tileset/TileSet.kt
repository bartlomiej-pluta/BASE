package com.bartlomiejpluta.base.editor.model.tileset

import javafx.scene.image.Image
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import java.nio.ByteBuffer


class TileSet(private val image: Image, val rows: Int, val columns: Int) {
    val tileWidth = image.width.toInt() / columns
    val tileHeight = image.height.toInt()  / rows
    val width = tileWidth * columns
    val height = tileHeight * rows

    val tiles: Array<Array<Tile>> =
        Array(rows) { row -> Array(columns) { column -> cropTile(row, column) } }

    private fun cropTile(row: Int, column: Int): Tile {
        val reader = image.pixelReader
        val buffer = ByteBuffer.allocate(tileWidth * tileHeight * 4)
        reader.getPixels(column * tileWidth, row * tileHeight, tileWidth, tileHeight, PixelFormat.getByteBgraInstance(), buffer, 4 * tileWidth)
        val tile = WritableImage(tileWidth, tileHeight)
        val writer = tile.pixelWriter
        writer.setPixels(0, 0, tileWidth, tileHeight, PixelFormat.getByteBgraInstance(), buffer, 4 * tileWidth)

        return Tile(this, row, column, tile)
    }

    fun getTile(row: Int, column: Int) = tiles[row][column]

    fun getTile(id: Int) = tiles[id / rows][id % columns]
}