package com.bartlomiejpluta.base.editor.asset.model

import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

abstract class Asset(directory: ObjectProperty<File>, val uid: String, val source: String, name: String) {
   val nameProperty = SimpleStringProperty(name)
   var name by nameProperty

   val fileProperty = createObjectBinding({ File(directory.value, source) }, directory)
   val file by fileProperty

   override fun toString() = "${this.javaClass.simpleName}[name=$name, uid=$uid]"
}