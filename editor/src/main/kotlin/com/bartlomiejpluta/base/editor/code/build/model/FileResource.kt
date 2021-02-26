package com.bartlomiejpluta.base.editor.code.build.model

import org.codehaus.commons.compiler.util.resource.Resource
import java.io.File
import java.io.InputStream

class FileResource(private val file: File) : Resource {

   override fun open(): InputStream = file.inputStream()

   override fun getFileName(): String = file.name

   override fun lastModified(): Long = file.lastModified()
}