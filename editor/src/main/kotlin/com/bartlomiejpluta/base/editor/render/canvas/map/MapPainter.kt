package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.EditorOptionsVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color

class MapPainter(
   private val mapVM: GameMapVM,
   private val brushVM: BrushVM,
   private val editorOptionsVM: EditorOptionsVM,
   private val paintingCallback: (MapPaintingTrace) -> Unit
) : Renderable, MapMouseEventHandler {
   private val tileWidth = mapVM.tileSet.tileWidth.toDouble()
   private val tileHeight = mapVM.tileSet.tileHeight.toDouble()

   private var mouseRow = -1
   private var mouseColumn = -1

   private var currentTrace: MapPaintingTrace? = null

   override fun render(gc: GraphicsContext) {
      val alpha = gc.globalAlpha
      gc.globalAlpha = 0.4

      brushVM.forEach { row, column, centerRow, centerColumn, tile ->
         renderTile(gc, row, column, centerRow, centerColumn, tile)
      }

      gc.globalAlpha = alpha
   }

   private fun renderTile(gc: GraphicsContext, row: Int, column: Int, centerRow: Int, centerColumn: Int, tile: Tile?) {
      val x = tileWidth * (mouseColumn - centerColumn + column)
      val y = tileHeight * (mouseRow - centerRow + row)

      when {
         tile != null -> renderPaintingBrushTile(gc, tile, x, y)
         else -> renderEraserTile(gc, x, y)
      }
   }

   private fun renderPaintingBrushTile(gc: GraphicsContext, tile: Tile, x: Double, y: Double) =
      gc.drawImage(tile.image, x, y)

   private fun renderEraserTile(gc: GraphicsContext, x: Double, y: Double) {
      gc.fill = Color.WHITE
      gc.fillRect(x, y, tileWidth, tileHeight)
   }

   override fun handleMouseInput(event: MapMouseEvent) {
      mouseRow = event.row
      mouseColumn = event.column

      when (event.type) {
         MouseEvent.MOUSE_PRESSED -> beginTrace(event)
         MouseEvent.MOUSE_DRAGGED -> proceedTrace(event)
         MouseEvent.MOUSE_RELEASED -> commitTrace(event)
      }
   }

   private fun beginTrace(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY && editorOptionsVM.selectedLayer >= 0) {
         currentTrace = MapPaintingTrace(mapVM, "Paint trace").apply {
            brushVM.forEach { row, column, centerRow, centerColumn, tile ->
               paint(editorOptionsVM.selectedLayer, mouseRow - centerRow + row, mouseColumn - centerColumn + column, tile)
            }
         }
      }
   }

   private fun proceedTrace(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         currentTrace?.apply {
            brushVM.forEach { row, column, centerRow, centerColumn, tile ->
               paint(editorOptionsVM.selectedLayer, mouseRow - centerRow + row, mouseColumn - centerColumn + column, tile)
            }
         }
      }
   }

   private fun commitTrace(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         currentTrace?.let {
            paintingCallback(it)
            currentTrace = null
         }
      }
   }
}