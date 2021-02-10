package com.bartlomiejpluta.base.editor.map.canvas

import com.bartlomiejpluta.base.editor.command.model.base.Undoable
import com.bartlomiejpluta.base.editor.map.model.layer.TileLayer
import com.bartlomiejpluta.base.editor.tileset.model.Tile
import com.bartlomiejpluta.base.editor.map.viewmodel.GameMapVM


data class MapPaintingTrace(val map: GameMapVM, override val commandName: String) : Undoable {
   private val trace = mutableListOf<Element>()

   fun paint(layerIndex: Int, row: Int, column: Int, tile: Tile?) {
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
      }
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
}