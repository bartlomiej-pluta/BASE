package com.bartlomiejpluta.base.editor.animation.asset

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class AnimationAssetData {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val fileProperty = SimpleObjectProperty<File>()
   var file by fileProperty

   val rowsProperty = SimpleIntegerProperty(1)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(1)
   var columns by columnsProperty

   val tileSetWidthProperty = SimpleIntegerProperty(1)
   var tileWidth by tileSetWidthProperty

   val tileSetHeightProperty = SimpleIntegerProperty(1)
   var tileHeight by tileSetHeightProperty
}