package com.bartlomiejpluta.base.editor.model.map.map

import com.bartlomiejpluta.base.editor.model.map.layer.Layer
import com.bartlomiejpluta.base.editor.model.tileset.TileSet
import javafx.beans.property.ReadOnlyDoubleWrapper
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*


class GameMap(val tileSet: TileSet, initialColumns: Int, initialRows: Int) {
   val layers = observableListOf<Layer>()
   val mapProperties = observableListOf<MapProperty>()

   val rowsProperty = SimpleIntegerProperty(initialRows)
   val rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(initialColumns)
   val columns by columnsProperty
}
