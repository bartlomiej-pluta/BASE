package com.bartlomiejpluta.base.editor.asset.model

import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableValue
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

abstract class Asset(sourceDirectory: ObservableValue<File>, compiledDirectory: ObservableValue<File>, val uid: String, val source: String, val binarySource: String, name: String) {
   constructor(directory: ObservableValue<File>, uid: String, source: String, name: String) : this(directory, directory, uid, source, source, name)

   val nameProperty = SimpleStringProperty(name)
   var name by nameProperty

   val fileProperty = createObjectBinding({ File(sourceDirectory.value, source) }, sourceDirectory)
   val file by fileProperty

   val binaryFileProperty = createObjectBinding({ File(compiledDirectory.value, binarySource) })
   val binaryFile by binaryFileProperty

   override fun toString() = "${this.javaClass.simpleName}[name=$name, uid=$uid]"
}