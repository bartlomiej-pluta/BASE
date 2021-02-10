package com.bartlomiejpluta.base.editor.project.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File

class Project {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val sourceDirectoryProperty = SimpleObjectProperty<File>()
   val sourceDirectory by sourceDirectoryProperty
}
