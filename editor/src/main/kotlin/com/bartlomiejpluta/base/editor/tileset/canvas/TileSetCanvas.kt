package com.bartlomiejpluta.base.editor.tileset.canvas

import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritableImage
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import kotlin.math.roundToInt

class TileSetCanvas(private val editorStateVM: EditorStateVM, private val selection: TileSetSelection) : Renderable,
   MapMouseEventHandler {
   private var mouseRow = -1
   private var mouseColumn = -1

   override fun render(gc: GraphicsContext) {
      gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

      if (editorStateVM.selectedLayer is TileLayer) {
         val tileSet = (editorStateVM.selectedLayer as TileLayer).tileSetProperty.value
         renderBackground(gc)
         renderTiles(gc, tileSet)
         renderGrid(gc, tileSet)
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

   private fun renderTiles(gc: GraphicsContext, tileSet: TileSet) {
      tileSet.forEach { row, column, tile ->
         gc.drawImage(tile.image, column * tile.image.width, row * tile.image.height)
      }
   }

   private fun renderGrid(gc: GraphicsContext, tileSet: TileSet) {
      gc.stroke = Color.BLACK
      gc.lineWidth = 0.5

      for (x in 0 until (gc.canvas.width / tileSet.tileWidth).roundToInt()) {
         gc.strokeLine(x.toDouble() * tileSet.tileWidth, 0.0, x.toDouble() * tileSet.tileWidth, gc.canvas.height)
      }

      for (y in 0 until (gc.canvas.height / tileSet.tileHeight).roundToInt()) {
         gc.strokeLine(0.0, y.toDouble() * tileSet.tileHeight, gc.canvas.width, y.toDouble() * tileSet.tileHeight)
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

      when (event.type) {
         MouseEvent.MOUSE_PRESSED -> beginSelection(event)
         MouseEvent.MOUSE_DRAGGED -> proceedSelection(event)
         MouseEvent.MOUSE_RELEASED -> finishSelection(event)
      }
   }

   private fun beginSelection(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         selection.begin(event.row.toDouble(), event.column.toDouble())
      }
   }

   private fun proceedSelection(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         selection.proceed(event.row.toDouble(), event.column.toDouble())
      }
   }

   private fun finishSelection(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         selection.finish(event.row.toDouble(), event.column.toDouble())
      }
   }

   companion object {
      private const val BACKGROUND_TILE_WIDTH = 5.0
      private const val BACKGROUND_TILE_HEIGHT = 5.0
      private val BACKGROUND_COLOR1 = Color.color(1.0, 1.0, 1.0, 1.0)
      private val BACKGROUND_COLOR2 = Color.color(0.95, 0.95, 0.95, 1.0)
   }
}