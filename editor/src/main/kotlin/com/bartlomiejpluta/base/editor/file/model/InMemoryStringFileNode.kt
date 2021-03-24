package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.property.SimpleLongProperty
import tornadofx.getValue
import tornadofx.toProperty

class InMemoryStringFileNode(name: String, extension: String, val content: String) : FileNode {
   override val nameProperty = "$name.$extension".toProperty()
   override val name by nameProperty

   override val extensionProperty = extension.toProperty()
   override val extension by extensionProperty

   override val nameWithoutExtensionProperty = name.toProperty()
   override val nameWithoutExtension by nameWithoutExtensionProperty

   override val absolutePathProperty = "".toProperty()
   override val absolutePath by absolutePathProperty

   override val type = FileType.FILE

   override val parent = null

   override val children = emptyList<FileNode>()

   override val lastModifiedProperty = SimpleLongProperty(0)
   override val lastModified by lastModifiedProperty

   override fun inputStream() = content.byteInputStream()
}