package com.bartlomiejpluta.base.editor.code.compiler

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import org.codehaus.commons.compiler.CompileException
import org.codehaus.janino.CompilerFactory
import org.springframework.stereotype.Component
import tornadofx.FX
import java.io.File

@Component
class JavaCompiler : ScriptCompiler {
   private val compilerFactory = CompilerFactory()

   override fun compile(sourceDirectory: FileSystemNode) {
      val files = sourceDirectory.allChildren.map(FileSystemNode::file).filter(File::isFile)
      val compiler = compilerFactory.newCompiler()

      try {
         compiler.compile(files.toTypedArray())
      } catch (e: CompileException) {
         FX.eventbus.fire(UpdateCompilationLogEvent(e.message ?: "", e.location))
      }
   }
}