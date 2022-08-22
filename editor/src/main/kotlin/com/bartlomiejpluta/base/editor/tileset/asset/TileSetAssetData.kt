package com.bartlomiejpluta.base.editor.tileset.asset

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class TileSetAssetData {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val rowsProperty = SimpleIntegerProperty(1)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(1)
   var columns by columnsProperty

   val tileWidthProperty = SimpleIntegerProperty(1)
   var tileWidth by tileWidthProperty

   val tileHeightProperty = SimpleIntegerProperty(1)
   var tileHeight by tileHeightProperty

   val fileProperty = SimpleObjectProperty<File>()
   var file by fileProperty
}