package com.bartlomiejpluta.base.editor.project.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Project {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty
}