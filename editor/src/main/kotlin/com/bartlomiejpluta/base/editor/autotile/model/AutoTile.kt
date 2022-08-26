package com.bartlomiejpluta.base.editor.autotile.model

import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.util.ImageUtil
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.image.Image
import tornadofx.div
import tornadofx.getValue

// Algorithm source: https://love2d.org/forums/viewtopic.php?t=7826
class AutoTile(uid: String, name: String, image: Image) {
   val uidProperty = ReadOnlyStringWrapper(uid)
   val uid by uidProperty

   val nameProperty = SimpleStringProperty(name)
   val name by nameProperty

   val imageProperty = SimpleObjectProperty(image)
   val image by imageProperty

   val rowsProperty = SimpleIntegerProperty(ROWS)
   val rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(COLUMNS)
   val columns by columnsProperty

   val tileWidthProperty = SimpleIntegerProperty(image.width.toInt() / columns)
   val tileWidth by tileWidthProperty

   val tileHeightProperty = SimpleIntegerProperty(image.height.toInt() / rows)
   val tileHeight by tileHeightProperty

   val widthProperty = SimpleIntegerProperty(tileWidth * columns)
   val width by widthProperty

   val heightProperty = SimpleIntegerProperty(tileHeight * rows)
   val height by heightProperty

   val halfWidthProperty = tileWidthProperty.div(2)
   val halfWidth by halfWidthProperty

   val halfHeightProperty = tileWidthProperty.div(2)
   val halfHeight by halfHeightProperty

   val islandSubTiles: Array<Image>
   val topLeftSubTiles: Array<Image>
   val topRightSubTiles: Array<Image>
   val bottomLeftSubTiles: Array<Image>
   val bottomRightSubTiles: Array<Image>

   init {
      val islandTile = cropTile(0, 0)
      val crossTile = cropTile(1, 0)
      val topLeftCornerTile = cropTile(0, 1)
      val topRightCornerTile = cropTile(1, 1)
      val bottomLeftCornerTile = cropTile(0, 2)
      val bottomRightCornerTile = cropTile(1, 2)

      /*
       * Indexes:
       *  0 - No connected tiles
       *  1 - Left tile is connected
       *  2 - Right tile is connected
       *  3 - Left and Right tiles are connected
       *  4 - Left, Right, and Center tiles are connected.
       */
      val (tl3, tr3, bl3, br3) = cutSubTiles(crossTile)
      val (tl0, tr2, bl1, br4) = cutSubTiles(topLeftCornerTile)
      val (tl1, tr0, bl4, br2) = cutSubTiles(topRightCornerTile)
      val (tl2, tr4, bl0, br1) = cutSubTiles(bottomLeftCornerTile)
      val (tl4, tr1, bl2, br0) = cutSubTiles(bottomRightCornerTile)

      islandSubTiles = cutSubTiles(islandTile)
      topLeftSubTiles = arrayOf(tl0, tl1, tl2, tl3, tl4)
      topRightSubTiles = arrayOf(tr0, tr1, tr2, tr3, tr4)
      bottomLeftSubTiles = arrayOf(bl0, bl1, bl2, bl3, bl4)
      bottomRightSubTiles = arrayOf(br0, br1, br2, br3, br4)
   }

   fun getTile(layer: AutoTileLayer, row: Int, column: Int): Array<Image> {
      var topLeft = 0
      var topRight = 0
      var bottomLeft = 0
      var bottomRight = 0

      // Top
      if (row > 0 && layer.layer[row - 1][column]) {
         topLeft += 2
         topRight += 1
      }

      // Bottom
      if (row < layer.rows - 1 && layer.layer[row + 1][column]) {
         bottomLeft += 1
         bottomRight += 2
      }

      // Left
      if (column > 0 && layer.layer[row][column - 1]) {
         topLeft += 1
         bottomLeft += 2
      }

      // Right
      if (column < layer.columns - 1 && layer.layer[row][column + 1]) {
         topRight += 2
         bottomRight += 1
      }

      // Top left
      if (row > 0 && column > 0 && layer.layer[row - 1][column - 1] && topLeft == 3) {
         topLeft = 4
      }

      // Top right
      if (row > 0 && column < layer.columns - 1 && layer.layer[row - 1][column + 1] && topRight == 3) {
         topRight = 4
      }

      // Bottom left
      if (row < layer.rows - 1 && column > 0 && layer.layer[row + 1][column - 1] && bottomLeft == 3) {
         bottomLeft = 4
      }

      // Bottom right
      if (row < layer.rows - 1 && column < layer.columns - 1 && layer.layer[row + 1][column + 1] && bottomRight == 3) {
         bottomRight = 4
      }

      if (topLeft == 0 && topRight == 0 && bottomLeft == 0 && bottomRight == 0) {
         return islandSubTiles
      }

      return arrayOf(topLeftSubTiles[topLeft], topRightSubTiles[topRight], bottomLeftSubTiles[bottomLeft], bottomRightSubTiles[bottomRight])
   }

   private fun cropTile(column: Int, row: Int) =
      ImageUtil.cropImage(image, column * tileWidth, row * tileHeight, tileWidth, tileHeight)

   private fun cutSubTiles(tile: Image): Array<Image> {
      val halfWidth = tileWidth / 2
      val halfHeight = tileHeight / 2
      val topLeft = ImageUtil.cropImage(tile, 0, 0, halfWidth, halfHeight)
      val topRight = ImageUtil.cropImage(tile, halfWidth, 0, halfWidth, halfHeight)
      val bottomLeft = ImageUtil.cropImage(tile, 0, halfHeight, halfWidth, halfHeight)
      val bottomRight = ImageUtil.cropImage(tile, halfWidth, halfHeight, halfWidth, halfHeight)

      return arrayOf(topLeft, topRight, bottomLeft, bottomRight)
   }

   companion object {
      const val ROWS = 3
      const val COLUMNS = 2
   }
}