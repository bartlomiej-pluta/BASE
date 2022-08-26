package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.map.model.brush.TileBrush
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.map.viewmodel.BrushVM
import com.bartlomiejpluta.base.editor.map.viewmodel.EditorStateVM
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM
import com.bartlomiejpluta.base.editor.render.input.MapMouseEvent
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import javafx.scene.input.MouseButton


data class TilePaintingTrace(val map: GameMapVM, override val commandName: String) : PaintingTrace {
   private val trace = mutableListOf<Element>()

   override var executed = false
      private set

   private fun paint(layerIndex: Int, row: Int, column: Int, tile: Tile?) {
      if (row >= map.rows || column >= map.columns || row < 0 || column < 0 || layerIndex < 0) {
         return
      }

      val layer = (map.layers[layerIndex] as TileLayer).layer
      val formerTile = layer[row][column]

      if (trace.isEmpty()) {
         trace += Element(layerIndex, row, column, formerTile, tile)
         layer[row][column] = tile
         return
      }

      val tileAlreadyPainted =
         trace.find { it.layerIndex == layerIndex && it.row == row && it.column == column } != null

      if (!tileAlreadyPainted) {
         trace += Element(layerIndex, row, column, formerTile, tile)
         layer[row][column] = tile
         executed = true
      }
   }

   override fun beginTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      (brushVM.item as TileBrush).forEachTileOnBrush { row, column, centerRow, centerColumn, tile ->
         paint(
            editorStateVM.selectedLayerIndex,
            editorStateVM.cursorRow - centerRow + row,
            editorStateVM.cursorColumn - centerColumn + column,
            when (mouseEvent.button) {
               MouseButton.PRIMARY -> tile
               else -> null
            }
         )
      }
   }

   override fun proceedTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {
      (brushVM.item as TileBrush).forEachTileOnBrush { row, column, centerRow, centerColumn, tile ->
         paint(
            editorStateVM.selectedLayerIndex,
            editorStateVM.cursorRow - centerRow + row,
            editorStateVM.cursorColumn - centerColumn + column,
            when (mouseEvent.button) {
               MouseButton.PRIMARY -> tile
               else -> null
            }
         )
      }
   }

   override fun commitTrace(editorStateVM: EditorStateVM, brushVM: BrushVM, mouseEvent: MapMouseEvent) {

   }

   override fun undo() {
      trace.forEach {
         (map.layers[it.layerIndex] as TileLayer).layer[it.row][it.column] = it.formerTile
      }
   }

   override fun redo() {
      trace.forEach {
         (map.layers[it.layerIndex] as TileLayer).layer[it.row][it.column] = it.tile
      }
   }

   companion object {
      private data class Element(
         val layerIndex: Int,
         val row: Int,
         val column: Int,
         val formerTile: Tile?,
         val tile: Tile?
      )
   }

   override val supportedButtons = arrayOf(MouseButton.PRIMARY, MouseButton.SECONDARY)
}