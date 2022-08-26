package com.bartlomiejpluta.base.editor.map.model.map

import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class GameMapBuilder {
   val nameProperty = SimpleStringProperty("")
   var name by nameProperty

   val tileWidthProperty = SimpleIntegerProperty(32)
   var tileWidth by tileWidthProperty

   val tileHeightProperty = SimpleIntegerProperty(32)
   var tileHeight by tileHeightProperty

   val rowsProperty = SimpleIntegerProperty(20)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(20)
   var columns by columnsProperty

   val handlerProperty = SimpleStringProperty()
   var handler by handlerProperty

   val fileProperty = SimpleStringProperty("")
   var file by fileProperty
}