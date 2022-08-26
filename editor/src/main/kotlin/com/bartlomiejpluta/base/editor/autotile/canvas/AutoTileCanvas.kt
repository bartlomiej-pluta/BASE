package com.bartlomiejpluta.base.editor.autotile.canvas

import com.bartlomiejpluta.base.editor.autotile.model.AutoTile
import com.bartlomiejpluta.base.editor.map.model.layer.AutoTileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import kotlin.math.roundToInt

class AutoTileCanvas(private val editorStateVM: EditorStateVM, private val gameMapVM: GameMapVM, private val selection: AutoTileSelection) : Renderable,
   MapMouseEventHandler {
   private var mouseRow = -1
   private var mouseColumn = -1

   override fun render(gc: GraphicsContext) {
      gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

      if (editorStateVM.selectedLayer is AutoTileLayer) {
         val autoTile = (editorStateVM.selectedLayer as AutoTileLayer).autoTile
         renderBackground(gc)
         renderTiles(gc, autoTile)
         renderGrid(gc, autoTile)
         selection.render(gc)
      }
   }

   private fun renderBackground(gc: GraphicsContext) {
      for (x in 0 until (gc.canvas.width / BACKGROUND_TILE_WIDTH).roundToInt()) {
         for (y in 0 until (gc.canvas.height / BACKGROUND_TILE_HEIGHT).roundToInt()) {
            gc.fill = when ((x + y) % 2) {
               0 -> BACKGROUND_COLOR1
               else -> BACKGROUND_COLOR2
            }

            gc.fillRect(x * BACKGROUND_TILE_WIDTH, y * BACKGROUND_TILE_HEIGHT, BACKGROUND_TILE_WIDTH, BACKGROUND_TILE_HEIGHT)
         }
      }
   }

   private fun renderTiles(gc: GraphicsContext, autoTile: AutoTile) {
      autoTile.islandSubTiles.forEachIndexed { i, (topLeft, topRight, bottomLeft, bottomRight) ->
         val x = (i % autoTile.columns) * gameMapVM.tileWidth
         val y = (i / autoTile.columns) * gameMapVM.tileHeight
         gc.drawImage(topLeft, x, y)
         gc.drawImage(topRight, x + gameMapVM.tileWidth/2, y)
         gc.drawImage(bottomLeft, x, y + gameMapVM.tileHeight/2)
         gc.drawImage(bottomRight, x + gameMapVM.tileWidth/2, y + gameMapVM.tileHeight/2)


      }
//      autoTile.forEach { row, column, tile ->
//         gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
//      }
   }

   private fun renderGrid(gc: GraphicsContext, autoTile: AutoTile) {
      gc.stroke = Color.BLACK
      gc.lineWidth = 0.5

      for (x in 0 until (gc.canvas.width / autoTile.tileWidth).roundToInt()) {
         gc.strokeLine(x.toDouble() * autoTile.tileWidth, 0.0, x.toDouble() * autoTile.tileWidth, gc.canvas.height)
      }

      for (y in 0 until (gc.canvas.height / autoTile.tileHeight).roundToInt()) {
         gc.strokeLine(0.0, y.toDouble() * autoTile.tileHeight, gc.canvas.width, y.toDouble() * autoTile.tileHeight)
      }

      val w = gc.canvas.width - 1
      val h = gc.canvas.height - 1

      gc.strokeLine(0.0, 0.0, w, 0.0)
      gc.strokeLine(w, 0.0, w, h)
      gc.strokeLine(0.0, h, w, h)
      gc.strokeLine(0.0, 0.0, 0.0, h)
   }

   override fun handleMouseInput(event: MapMouseEvent) {
      mouseRow = event.row
      mouseColumn = event.column

      if(event.type == MouseEvent.MOUSE_PRESSED && event.button == MouseButton.PRIMARY) {
         selection.select(event.row.toDouble(), event.column.toDouble())
      }
   }


   companion object {
      private const val BACKGROUND_TILE_WIDTH = 5.0
      private const val BACKGROUND_TILE_HEIGHT = 5.0
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.95, 0.95, 0.95, 1.0)
   }
}