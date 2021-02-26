package com.bartlomiejpluta.base.editor.code.build.model

import org.codehaus.commons.compiler.util.resource.Resource
import java.io.InputStream

class ClasspathResource(private val resource: org.springframework.core.io.Resource) : Resource {

   override fun open(): InputStream = resource.inputStream

   override fun getFileName(): String = resource.filename ?: ""

   override fun lastModified(): Long = resource.lastModified()
}