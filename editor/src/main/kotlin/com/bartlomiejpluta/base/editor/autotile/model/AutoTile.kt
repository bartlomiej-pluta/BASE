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
class AutoTile(uid: String, name: String, image: Image, rows: Int, columns: Int, layout: AutoTileLayout) {
   val uidProperty = ReadOnlyStringWrapper(uid)
   val uid by uidProperty

   val nameProperty = SimpleStringProperty(name)
   val name by nameProperty

   val imageProperty = SimpleObjectProperty(image)
   val image by imageProperty

   val rowsProperty = SimpleIntegerProperty(rows)
   val rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(columns)
   val columns by columnsProperty

   val tileSetWidthProperty = SimpleIntegerProperty(image.width.toInt() / columns)
   val tileSetWidth by tileSetWidthProperty

   val tileSetHeightProperty = SimpleIntegerProperty(image.height.toInt() / rows)
   val tileSetHeight by tileSetHeightProperty

   val tileWidthProperty = tileSetWidthProperty.div(layout.columns)
   val tileWidth by tileWidthProperty

   val tileHeightProperty = tileSetHeightProperty.div(layout.rows)
   val tileHeight by tileHeightProperty

   val widthProperty = SimpleIntegerProperty(image.width.toInt())
   val width by widthProperty

   val heightProperty = SimpleIntegerProperty(image.height.toInt() )
   val height by heightProperty
   
   val layoutProperty = SimpleObjectProperty(layout)
   val layout by layoutProperty


   lateinit var islandSubTiles: Array<Array<Image>>
      private set
   
   lateinit var topLeftSubTiles: Array<Array<Image>>
      private set
   
   lateinit var topRightSubTiles: Array<Array<Image>>
      private set
   
   lateinit var bottomLeftSubTiles: Array<Array<Image>>
      private set
   
   lateinit var bottomRightSubTiles: Array<Array<Image>>
      private set

   init {
      when(layout) {
         AutoTileLayout.LAYOUT_2X3 -> init2x3()
         AutoTileLayout.LAYOUT_2X2 -> init2x2()
      }
   }
   
   private fun init2x3() {
      val islandSubTiles: MutableList<Array<Image>> = mutableListOf()
      val topLeftSubTiles: MutableList<Array<Image>> = mutableListOf()
      val topRightSubTiles: MutableList<Array<Image>> = mutableListOf()
      val bottomLeftSubTiles: MutableList<Array<Image>> = mutableListOf()
      val bottomRightSubTiles: MutableList<Array<Image>> = mutableListOf()

      for (i in 0 until columns * rows) {
         val tileSet = cropTileSet(i)
         val islandTile = cropTile(tileSet, 0, 0)
         val crossTile = cropTile(tileSet, 1, 0)
         val topLeftCornerTile = cropTile(tileSet, 0, 1)
         val topRightCornerTile = cropTile(tileSet, 1, 1)
         val bottomLeftCornerTile = cropTile(tileSet, 0, 2)
         val bottomRightCornerTile = cropTile(tileSet, 1, 2)

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

         islandSubTiles += cutSubTiles(islandTile)
         topLeftSubTiles += arrayOf(tl0, tl1, tl2, tl3, tl4)
         topRightSubTiles += arrayOf(tr0, tr1, tr2, tr3, tr4)
         bottomLeftSubTiles += arrayOf(bl0, bl1, bl2, bl3, bl4)
         bottomRightSubTiles += arrayOf(br0, br1, br2, br3, br4)
      }

      this.islandSubTiles = islandSubTiles.toTypedArray()
      this.topLeftSubTiles = topLeftSubTiles.toTypedArray()
      this.topRightSubTiles = topRightSubTiles.toTypedArray()
      this.bottomLeftSubTiles = bottomLeftSubTiles.toTypedArray()
      this.bottomRightSubTiles = bottomRightSubTiles.toTypedArray()
   }

   private fun init2x2() {
      val topLeftSubTiles: MutableList<Array<Image>> = mutableListOf()
      val topRightSubTiles: MutableList<Array<Image>> = mutableListOf()
      val bottomLeftSubTiles: MutableList<Array<Image>> = mutableListOf()
      val bottomRightSubTiles: MutableList<Array<Image>> = mutableListOf()

      for (i in 0 until columns * rows) {
         val tileSet = cropTileSet(i)
         val topLeftCornerTile = cropTile(tileSet, 0, 0)
         val topRightCornerTile = cropTile(tileSet, 1, 0)
         val bottomLeftCornerTile = cropTile(tileSet, 0, 1)
         val bottomRightCornerTile = cropTile(tileSet, 1, 1)

         /*
          * Indexes:
          *  0 - No connected tiles
          *  1 - Left tile is connected
          *  2 - Right tile is connected
          *  3 - Left, Right, and Center tiles are connected.
          */
         val (tl0, tr2, bl1, br3) = cutSubTiles(topLeftCornerTile)
         val (tl1, tr0, bl3, br2) = cutSubTiles(topRightCornerTile)
         val (tl2, tr3, bl0, br1) = cutSubTiles(bottomLeftCornerTile)
         val (tl3, tr1, bl2, br0) = cutSubTiles(bottomRightCornerTile)

         topLeftSubTiles += arrayOf(tl0, tl1, tl2, tl3)
         topRightSubTiles += arrayOf(tr0, tr1, tr2, tr3)
         bottomLeftSubTiles += arrayOf(bl0, bl1, bl2, bl3)
         bottomRightSubTiles += arrayOf(br0, br1, br2, br3)
      }

      this.topLeftSubTiles = topLeftSubTiles.toTypedArray()
      this.topRightSubTiles = topRightSubTiles.toTypedArray()
      this.bottomLeftSubTiles = bottomLeftSubTiles.toTypedArray()
      this.bottomRightSubTiles = bottomRightSubTiles.toTypedArray()

      this.islandSubTiles = this.topLeftSubTiles
   }

   fun getTile(layer: AutoTileLayer, row: Int, column: Int, connect: Boolean) = when(layout!!) {
      AutoTileLayout.LAYOUT_2X3 -> getTile2x3(layer, row, column, connect)
      AutoTileLayout.LAYOUT_2X2 -> getTile2x2(layer, row, column, connect)
   }

   private fun isAdjacent(currentId: Int, centerId: Int, connect: Boolean) = when (connect) {
      true -> currentId > 0
      false -> currentId == centerId
   }

   private fun getTile2x3(layer: AutoTileLayer, row: Int, column: Int, connect: Boolean): Array<Image> {
      var topLeft = 0
      var topRight = 0
      var bottomLeft = 0
      var bottomRight = 0

      val id = layer.layer[row][column]

      // Top
      if (row > 0 && isAdjacent(layer.layer[row - 1][column] , id, connect)) {
         topLeft += 2
         topRight += 1
      }

      // Bottom
      if (row < layer.rows - 1 && isAdjacent(layer.layer[row + 1][column] , id, connect)) {
         bottomLeft += 1
         bottomRight += 2
      }

      // Left
      if (column > 0 && isAdjacent(layer.layer[row][column - 1] , id, connect)) {
         topLeft += 1
         bottomLeft += 2
      }

      // Right
      if (column < layer.columns - 1 && isAdjacent(layer.layer[row][column + 1] , id, connect)) {
         topRight += 2
         bottomRight += 1
      }

      // Top left
      if (row > 0 && column > 0 && isAdjacent(layer.layer[row - 1][column - 1] , id, connect) && topLeft == 3) {
         topLeft = 4
      }

      // Top right
      if (row > 0 && column < layer.columns - 1 && isAdjacent(layer.layer[row - 1][column + 1] , id, connect) && topRight == 3) {
         topRight = 4
      }

      // Bottom left
      if (row < layer.rows - 1 && column > 0 && isAdjacent(layer.layer[row + 1][column - 1] , id, connect) && bottomLeft == 3) {
         bottomLeft = 4
      }

      // Bottom right
      if (row < layer.rows - 1 && column < layer.columns - 1 && isAdjacent(layer.layer[row + 1][column + 1] , id, connect) && bottomRight == 3) {
         bottomRight = 4
      }

      if (topLeft == 0 && topRight == 0 && bottomLeft == 0 && bottomRight == 0) {
         return islandSubTiles!![id - 1]
      }

      return arrayOf(
         topLeftSubTiles[id - 1][topLeft],
         topRightSubTiles[id - 1][topRight],
         bottomLeftSubTiles[id - 1][bottomLeft],
         bottomRightSubTiles[id - 1][bottomRight]
      )
   }

   private fun getTile2x2(layer: AutoTileLayer, row: Int, column: Int, connect: Boolean): Array<Image> {
      var topLeft = 0
      var topRight = 0
      var bottomLeft = 0
      var bottomRight = 0

      val id = layer.layer[row][column]

      // Top
      if (row > 0 && isAdjacent(layer.layer[row - 1][column] , id, connect)) {
         topLeft += 2
         topRight += 1
      }

      // Bottom
      if (row < layer.rows - 1 && isAdjacent(layer.layer[row + 1][column] , id, connect)) {
         bottomLeft += 1
         bottomRight += 2
      }

      // Left
      if (column > 0 && isAdjacent(layer.layer[row][column - 1] , id, connect)) {
         topLeft += 1
         bottomLeft += 2
      }

      // Right
      if (column < layer.columns - 1 && isAdjacent(layer.layer[row][column + 1] , id, connect)) {
         topRight += 2
         bottomRight += 1
      }

      return arrayOf(
         topLeftSubTiles[id - 1][topLeft],
         topRightSubTiles[id - 1][topRight],
         bottomLeftSubTiles[id - 1][bottomLeft],
         bottomRightSubTiles[id - 1][bottomRight]
      )
   }

   private fun cropTileSet(id: Int): Image {
      return ImageUtil.cropImage(image, tileSetWidth * (id % columns), tileSetHeight * (id / columns), tileSetWidth, tileSetHeight)
   }

   private fun cropTile(tileSet: Image, column: Int, row: Int): Image {
      return ImageUtil.cropImage(tileSet, column * tileWidth, row * tileHeight, tileWidth, tileHeight)
   }

   private fun cutSubTiles(tile: Image): Array<Image> {
      val halfWidth = tileWidth / 2
      val halfHeight = tileHeight / 2
      val topLeft = ImageUtil.cropImage(tile, 0, 0, halfWidth, halfHeight)
      val topRight = ImageUtil.cropImage(tile, halfWidth, 0, halfWidth, halfHeight)
      val bottomLeft = ImageUtil.cropImage(tile, 0, halfHeight, halfWidth, halfHeight)
      val bottomRight = ImageUtil.cropImage(tile, halfWidth, halfHeight, halfWidth, halfHeight)

      return arrayOf(topLeft, topRight, bottomLeft, bottomRight)
   }
}