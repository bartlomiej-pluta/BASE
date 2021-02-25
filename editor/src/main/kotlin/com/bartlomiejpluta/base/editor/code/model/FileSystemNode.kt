package com.bartlomiejpluta.base.editor.code.model

import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue
import tornadofx.toProperty
import java.io.File
import java.nio.file.Path

class FileSystemNode(file: File, val parent: FileSystemNode? = null) {
   val fileProperty = file.toProperty()
   var file by fileProperty
      private set

   val isFile = file.isFile
   val isDirectory = file.isDirectory

   val children = observableListOf<FileSystemNode>()
   val allChildren: List<FileSystemNode>
      get() = children + children.flatMap { it.allChildren }

   init {
      refreshChildren()
   }

   private fun refreshChildren() {
      if (isDirectory) {
         children.clear()
         file.listFiles()?.map { FileSystemNode(it, this) }?.let { children.addAll(it) }
      }
   }

   fun rename(name: String) {
      val newFile = File(file.parent, name)
      file.renameTo(newFile)
      file = newFile
   }

   fun delete() {
      val deleted = when {
         isFile -> file.delete()
         isDirectory -> file.deleteRecursively()
         else -> false
      }

      if (deleted) {
         parent?.children?.remove(this)
      }
   }

   fun refresh() {
      refreshChildren()
   }

   fun createNode(path: String): FileSystemNode {
      val segments = Path.of(path.replace("..", "."))

      return segments.foldIndexed(this) { index, parent, segment ->
         val file = File(parent.file, segment.toString())

         when {
            index < segments.count() - 1 -> file.mkdirs()
            else -> file.createNewFile()
         }

         when (val child = findChild(file)) {
            null -> FileSystemNode(file, parent).also { parent.children += it }
            else -> child
         }
      }
   }

   private fun findChild(file: File): FileSystemNode? {
      return children.firstOrNull { it.file.name == file.name }
   }

   fun findByFile(file: File) = allChildren.firstOrNull { it.file.equals(file) }
}