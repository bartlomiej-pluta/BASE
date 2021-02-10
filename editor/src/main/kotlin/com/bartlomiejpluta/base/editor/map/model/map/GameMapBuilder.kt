package com.bartlomiejpluta.base.editor.map.model.map

import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class GameMapBuilder {
   val tileSetProperty = SimpleObjectProperty(TILESET)
   var tileSet by tileSetProperty

   val nameProperty = SimpleStringProperty("")
   var name by nameProperty

   val rowsProperty = SimpleIntegerProperty(1)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(1)
   var columns by columnsProperty

   fun build() = GameMap(tileSet).apply {
      name = this@GameMapBuilder.name
      rows = this@GameMapBuilder.rows
      columns = this@GameMapBuilder.columns
   }

   companion object {
      // TODO(Hardcoded tileset here - to remove when tileset is choosable from map creation wizard)
      private val TILESET = TileSet(ResourceLookup(this).image("/textures/tileset.png"), 160, 8)
   }
}