package com.bartlomiejpluta.base.editor.audio.asset

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class SoundAssetData {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val fileProperty = SimpleObjectProperty<File>()
   var file by fileProperty
}