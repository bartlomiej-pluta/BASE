package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.value.ObservableValue
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

interface FileNode {
   val nameProperty: ObservableValue<String>
   val name: String

   val extensionProperty: ObservableValue<String>
   val extension: String

   val absolutePathProperty: ObservableValue<String>
   val absolutePath: String

   val type: FileType

   val parent: FileNode?
   val children: Iterable<FileNode>
   val allChildren: Iterable<FileNode>

   fun delete()
   fun rename(name: String)
   fun refresh()
   fun createNode(path: String): FileNode

   fun inputStream(): InputStream
   fun outputStream(): OutputStream

   fun writeBytes(array: ByteArray)
   fun writeText(text: String, charset: Charset = Charsets.UTF_8) = writeBytes(text.toByteArray(charset))
   fun readText(charset: Charset = Charsets.UTF_8) = inputStream().reader(charset).readText()
}