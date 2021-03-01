package com.bartlomiejpluta.base.editor.code.api

import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.file.model.ResourceFileNode
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.BufferedReader

@Component
class APIProvider {

   @Value("classpath:api.idx")
   private lateinit var apiIndex: Resource

   val apiNode: FileNode by lazy { loadNode() }

   private fun loadNode() = ResourceFileNode.root(API_DIR).apply {
      apiIndex.inputStream.bufferedReader().use(BufferedReader::readLines).forEach(this::createNode)
   }

   companion object {
      val API_DIR = "api"
   }
}