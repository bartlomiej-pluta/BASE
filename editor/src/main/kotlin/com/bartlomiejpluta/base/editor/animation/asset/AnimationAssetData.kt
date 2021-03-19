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

   val rowsProperty = SimpleIntegerProperty()
   var rows by rowsProperty

   val columnsProperty = SimpleIntegerProperty()
   var columns by columnsProperty
}