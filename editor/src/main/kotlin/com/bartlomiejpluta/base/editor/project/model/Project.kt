package com.bartlomiejpluta.base.editor.project.model

import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue
import java.io.File

class Project {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val sourceDirectoryProperty = SimpleObjectProperty<File>()
   val sourceDirectory by sourceDirectoryProperty

   val maps = observableListOf<GameMapAsset>()
}
