package com.bartlomiejpluta.base.editor.file.model

import javafx.beans.binding.Bindings.createLongBinding
import javafx.beans.binding.Bindings.createStringBinding
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue
import tornadofx.toProperty
import java.io.File
import java.nio.file.Path

class FileSystemNode(file: File, override val parent: FileSystemNode? = null) : FileNode {
   private val fileProperty = file.toProperty()
   var file by fileProperty
      private set

   override val nameProperty = createStringBinding({ fileProperty.value.name }, fileProperty)
   override val name by nameProperty

   override val extensionProperty = createStringBinding({ fileProperty.value.extension }, fileProperty)
   override val extension by extensionProperty

   override val absolutePathProperty = createStringBinding({ fileProperty.value.absolutePath }, fileProperty)
   override val absolutePath by absolutePathProperty

   override val type = when {
      file.isFile -> FileType.FILE
      file.isDirectory -> FileType.DIRECTORY
      else -> throw IllegalStateException("Unsupported file type")
   }

   override val children = observableListOf<FileSystemNode>()

   override val lastModifiedProperty = createLongBinding({ fileProperty.value.lastModified() }, fileProperty)
   override val lastModified by lastModifiedProperty

   init {
      refreshChildren()
      fileProperty.addListener { _, _, f -> println(f) }
      nameProperty.addListener { _, _, f -> println(f) }
   }

   private fun refreshChildren() {
      if (type == FileType.DIRECTORY) {
         children.clear()
         file.listFiles()?.map { FileSystemNode(it, this) }?.let { children.addAll(it) }
      }
   }

   override fun rename(name: String) {
      val newFile = File(file.parent, name)
      file.renameTo(newFile)
      file = newFile
   }

   override fun delete() {
      val deleted = when (type) {
         FileType.FILE -> file.delete()
         FileType.DIRECTORY -> file.deleteRecursively()
      }

      if (deleted) {
         parent?.children?.remove(this)
      }
   }

   override fun refresh() {
      refreshChildren()
   }

   override fun createNode(path: String): FileSystemNode {
      val segments = Path.of(path.replace("..", "."))

      return segments.foldIndexed(this) { index, parent, segment ->
         val file = File(parent.file, segment.toString())

         when {
            index < segments.count() - 1 -> file.mkdirs()
            else -> file.createNewFile()
         }

         when (val child = parent.children.firstOrNull { it.name == file.name }) {
            null -> FileSystemNode(file, parent).also { parent.children += it }
            else -> child
         }
      }
   }

   override fun inputStream() = file.inputStream()

   override fun outputStream() = file.outputStream()

   fun findByFile(file: File) = allChildren.firstOrNull { it.absolutePath == file.absolutePath }
}