package com.bartlomiejpluta.base.editor.code.compiler

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import org.codehaus.commons.compiler.CompileException
import org.codehaus.janino.CompilerFactory
import org.springframework.stereotype.Component
import tornadofx.FX
import java.io.File

@Component
class JaninoCompiler : ScriptCompiler {
   private val compilerFactory = CompilerFactory()

   override fun compile(sourceDirectory: FileSystemNode) {
      val files = sourceDirectory.allChildren.map(FileSystemNode::file).filter(File::isFile)
      val compiler = compilerFactory.newCompiler()

      // FIXME:
      // For some reason the compiler's error handler does not want to catch
      // syntax errors. The only way to catch it is just catching CompileExceptions
      try {
         compiler.compile(files.toTypedArray())
         FX.eventbus.fire(UpdateCompilationLogEvent(UpdateCompilationLogEvent.Severity.INFO, "Compilation success"))
      } catch (e: CompileException) {

         // Because the Janino compiler assemblies the message with the location
         // in the LocatedException.getMessage() method, we just need to remove it
         // to have a plain message along with the plain location as separated objects
         val locationIndex = e.location?.toString()?.length ?: 0
         val message = e.message?.substring(locationIndex) ?: ""
         FX.eventbus.fire(UpdateCompilationLogEvent(UpdateCompilationLogEvent.Severity.ERROR, message, e.location))
      }
   }
}