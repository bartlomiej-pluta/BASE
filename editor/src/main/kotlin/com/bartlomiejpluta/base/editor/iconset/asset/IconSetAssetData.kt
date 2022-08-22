package com.bartlomiejpluta.base.editor.iconset.asset

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class IconSetAssetData {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val rowsProperty = SimpleIntegerProperty(1)
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty(1)
   var columns by columnsProperty

   val iconWidthProperty = SimpleIntegerProperty(1)
   var iconWidth by iconWidthProperty

   val iconHeightProperty = SimpleIntegerProperty(1)
   var iconHeight by iconHeightProperty

   val fileProperty = SimpleObjectProperty<File>()
   var file by fileProperty
}