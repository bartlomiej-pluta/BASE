package com.bartlomiejpluta.base.editor.model.map.layer

import com.bartlomiejpluta.base.editor.model.tileset.Tile
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class TileLayer(name: String, val layer: Array<Array<Tile?>>) : Layer {

   fun setTile(row: Int, column: Int, tile: Tile?) = apply { layer[row][column] = tile }

   override val nameProperty = SimpleStringProperty(name)

   override var name: String by nameProperty

   companion object {
      fun empty(name: String, rows: Int ,columns: Int) = TileLayer(name, Array(rows) { Array(columns) { null } })
   }
}