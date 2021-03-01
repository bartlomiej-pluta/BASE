package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.code.api.APIProvider
import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.model.FileNodeResourceAdapter
import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.event.AppendBuildLogsEvent
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import org.codehaus.commons.compiler.CompileException
import org.codehaus.commons.compiler.util.resource.Resource
import org.codehaus.janino.CompilerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.FX.Companion.eventbus
import java.io.File

@Component
class JaninoCompiler : Compiler {
   private val compilerFactory = CompilerFactory()

   @Autowired
   private lateinit var apiProvider: APIProvider

   override fun compile(sourceDirectory: FileSystemNode, targetDirectory: File) = try {
      tryToCompile(sourceDirectory, targetDirectory)

      /* Because Janino parser does not provide error handler for parsers:
      *
      * > Notice that there is no Parser.setErrorHandler() method, but parse errors always throw a
      * > CompileException. The reason being is that there is no reasonable way to recover from parse errors and
      * > continue parsing, so there is no need to install a custom parse error handler.
      * > (org.codehaus.janino.Parser.java)
      *
      * the try-catch statement is required here to catch all parsing exceptions and re-throw them
      * as BuildException
      */
   } catch (e: CompileException) {
      val locationIndex = e.location?.toString()?.length?.plus(2) ?: 0
      val message = e.message?.substring(locationIndex)

      throw BuildException(Severity.ERROR, "Compiler", e.location, message, e)
   }

   private fun tryToCompile(sourceDirectory: FileSystemNode, targetDirectory: File) {
      val compilationUnits = prepareCompilationUnits(sourceDirectory)

      compilerFactory.newCompiler().apply {
         setDestinationDirectory(targetDirectory, false)

         setWarningHandler { handle, message, location ->
            eventbus.fire(AppendBuildLogsEvent(Severity.WARNING, "$message ($handle)", location, "Compiler"))
         }

         compile(compilationUnits)
      }
   }

   private fun prepareCompilationUnits(sourceDirectory: FileSystemNode): Array<Resource> {
      val sources = sourceDirectory.allChildren
      val api = apiProvider.apiNode.allChildren
      return (sources + api)
         .filter { it.type == FileType.FILE }
         .map(::FileNodeResourceAdapter)
         .toTypedArray()
   }
}
