package com.bartlomiejpluta.base.editor.autotile.asset

import com.bartlomiejpluta.base.editor.autotile.model.AutoTile
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class AutoTileAssetData {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val rowsProperty = SimpleIntegerProperty(1)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(1)
   var columns by columnsProperty

   val tileSetWidthProperty = SimpleIntegerProperty(1)
   var tileWidth by tileSetWidthProperty

   val tileSetHeightProperty = SimpleIntegerProperty(1)
   var tileHeight by tileSetHeightProperty

   val fileProperty = SimpleObjectProperty<File>()
   var file by fileProperty
}