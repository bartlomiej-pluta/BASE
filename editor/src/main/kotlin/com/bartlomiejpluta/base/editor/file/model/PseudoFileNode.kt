package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.property.SimpleLongProperty
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.toProperty

class PseudoFileNode(name: String, absolutePath: String, override val type: FileType) : FileNode {

   override val nameProperty = name.toProperty()
   override val name by nameProperty

   override val extensionProperty = "".toProperty()
   override val extension by extensionProperty

   override val nameWithoutExtensionProperty = nameProperty
   override val nameWithoutExtension by nameWithoutExtensionProperty

   override val absolutePathProperty = absolutePath.toProperty()
   override val absolutePath by absolutePathProperty

   override val lastModifiedProperty = SimpleLongProperty(0L)
   override val lastModified by lastModifiedProperty

   override val parent = null

   override val children = observableListOf<FileNode>()

   companion object {
      fun emptyRoot() = PseudoFileNode("", "", FileType.DIRECTORY)
   }
}