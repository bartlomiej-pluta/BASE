package com.bartlomiejpluta.base.editor.code.build.model

import com.bartlomiejpluta.base.editor.file.model.FileNode
import org.codehaus.commons.compiler.util.resource.Resource

class FileNodeResourceAdapter(private val fileNode: FileNode) : Resource {
   override fun open() = fileNode.inputStream()
   override fun getFileName() = fileNode.absolutePath
   override fun lastModified() = fileNode.lastModified
}