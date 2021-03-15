package com.bartlomiejpluta.base.editor.file.model

import com.bartlomiejpluta.base.editor.asset.model.Asset
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyLongWrapper
import javafx.beans.property.ReadOnlyStringWrapper
import tornadofx.getValue
import java.io.File

abstract class ScriptAssetFileNode(directory: ObjectProperty<File>, uid: String, extension: String, name: String) :
   Asset(directory, uid, "$uid.$extension", name), FileNode {

   final override val extensionProperty = ReadOnlyStringWrapper(extension)
   override val extension by extensionProperty

   final override val nameWithoutExtensionProperty = ReadOnlyStringWrapper(name)
   override val nameWithoutExtension by nameWithoutExtensionProperty

   final override val absolutePathProperty = ReadOnlyStringWrapper(file.absolutePath)
   override val absolutePath by absolutePathProperty

   override val type = FileType.FILE
   override val parent = null

   override val children = emptyList<FileNode>()

   final override val lastModifiedProperty = ReadOnlyLongWrapper(file.lastModified())
   override val lastModified by lastModifiedProperty

   override fun inputStream() = file.inputStream()

   override fun outputStream() = file.outputStream()
}