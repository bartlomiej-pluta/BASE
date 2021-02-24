package com.bartlomiejpluta.base.editor.code.model

import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue
import tornadofx.toProperty
import java.io.File

class FileSystemNode(file: File, val parent: FileSystemNode? = null) {
   val fileProperty = file.toProperty()
   var file by fileProperty
      private set

   val isFile = file.isFile
   val isDirectory = file.isDirectory

   val children = observableListOf<FileSystemNode>()

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
}