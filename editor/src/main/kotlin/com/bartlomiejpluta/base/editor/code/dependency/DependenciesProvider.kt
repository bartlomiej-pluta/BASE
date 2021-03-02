package com.bartlomiejpluta.base.editor.code.dependency

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.File

@Component
class DependenciesProvider {

   @Value("classpath:dependencies/*.jar")
   private lateinit var dependencies: Array<Resource>

   fun provideDependenciesTo(root: File) = dependencies
      .filter { it.filename != null }
      .map {
         File(root, it.filename!!).apply {
            it.inputStream.transferTo(outputStream())
         }
      }
}