package com.bartlomiejpluta.base.editor.render.canvas.map

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.render.canvas.input.MapMouseEventHandler
import com.bartlomiejpluta.base.editor.render.model.Renderable
import com.bartlomiejpluta.base.editor.viewmodel.map.BrushVM
import com.bartlomiejpluta.base.editor.viewmodel.map.GameMapVM
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

class MapPainter(
   private val mapVM: GameMapVM,
   private val brushVM: BrushVM,
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
         tile?.let {
            renderTile(gc, it, column, row, centerRow, centerColumn)
         }
      }

      gc.globalAlpha = alpha

   }

   private fun renderTile(gc: GraphicsContext, tile: Tile, column: Int, row: Int, centerRow: Int, centerColumn: Int) {
      gc.drawImage(
         tile.image,
         tileWidth * (mouseColumn - centerColumn + column),
         tileHeight * (mouseRow - centerRow + row)
      )
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
      if (event.button == MouseButton.PRIMARY && mapVM.selectedLayer >= 0) {
         currentTrace = MapPaintingTrace(mapVM, "Paint trace").apply {
            brushVM.forEach { row, column, centerRow, centerColumn, tile ->
               paint(map.selectedLayer, mouseRow - centerRow + row, mouseColumn - centerColumn + column, tile)
            }
         }
      }
   }

   private fun proceedTrace(event: MapMouseEvent) {
      if (event.button == MouseButton.PRIMARY) {
         currentTrace?.apply {
            brushVM.forEach { row, column, centerRow, centerColumn, tile ->
               paint(map.selectedLayer, mouseRow - centerRow + row, mouseColumn - centerColumn + column, tile)
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