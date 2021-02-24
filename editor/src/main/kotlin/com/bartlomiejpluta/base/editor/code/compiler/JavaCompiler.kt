package com.bartlomiejpluta.base.editor.code.compiler

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import org.codehaus.janino.CompilerFactory
import org.springframework.stereotype.Component
import java.io.File

@Component
class JavaCompiler : ScriptCompiler {
   private val compilerFactory = CompilerFactory()

   override fun compile(sourceDirectory: FileSystemNode) {
      val files = sourceDirectory.allChildren.map(FileSystemNode::file).filter(File::isFile)
      val compiler = compilerFactory.newCompiler()

      compiler.compile(files.toTypedArray())
   }
}