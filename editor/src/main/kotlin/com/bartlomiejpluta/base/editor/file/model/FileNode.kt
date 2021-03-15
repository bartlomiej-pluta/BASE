package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

interface FileNode {
   val nameProperty: ObservableValue<String>
   val name: String

   val extensionProperty: ObservableValue<String>
   val extension: String

   val nameWithoutExtensionProperty: ObservableValue<String>
   val nameWithoutExtension: String

   val absolutePathProperty: ObservableValue<String>
   val absolutePath: String

   val type: FileType

   val parent: FileNode?
   val children: Iterable<FileNode>
   val allChildren: List<FileNode>
      get() = children + children.flatMap { it.allChildren }

   val lastModifiedProperty: ObservableLongValue
   val lastModified: Long

   fun delete(): Unit = throw UnsupportedOperationException()
   fun rename(name: String): Unit = throw UnsupportedOperationException()
   fun refresh(): Unit = throw UnsupportedOperationException()
   fun createNode(path: String): FileNode = throw UnsupportedOperationException()

   fun inputStream(): InputStream = throw UnsupportedOperationException()
   fun outputStream(): OutputStream = throw UnsupportedOperationException()

   fun readText(charset: Charset = Charsets.UTF_8) = inputStream().reader(charset).use { it.readText() }
   fun writeText(text: String, charset: Charset = Charsets.UTF_8) = writeBytes(text.toByteArray(charset))
   fun writeBytes(array: ByteArray) = outputStream().use { it.write(array) }
}