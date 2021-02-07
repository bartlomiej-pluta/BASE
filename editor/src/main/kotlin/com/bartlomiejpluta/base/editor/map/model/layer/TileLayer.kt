package com.bartlomiejpluta.base.editor.map.model.layer

import com.bartlomiejpluta.base.editor.tileset.model.Tile
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class TileLayer(name: String, rows: Int, columns: Int) : Layer {
   var layer: Array<Array<Tile?>> = Array(rows) { Array(columns) { null } }
      private set

   override val nameProperty = SimpleStringProperty(name)
   override var name: String by nameProperty

   override fun resize(rows: Int, columns: Int) {
      layer = Array(rows) { row ->
         Array(columns) { column ->
            layer.getOrNull(row) ?. getOrNull(column)
         }
      }
   }
}