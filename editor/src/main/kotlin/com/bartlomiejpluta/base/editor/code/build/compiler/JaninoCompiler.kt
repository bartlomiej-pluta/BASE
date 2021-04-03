package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.model.FileNodeResourceAdapter
import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.common.logs.model.Location
import com.bartlomiejpluta.base.editor.event.AppendBuildLogsEvent
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import org.codehaus.commons.compiler.CompileException
import org.codehaus.janino.CompilerFactory
import org.springframework.stereotype.Component
import tornadofx.FX.Companion.eventbus
import java.io.File

@Component
class JaninoCompiler : Compiler {
   private val compilerFactory = CompilerFactory()

   override fun compile(sourceDirectories: Array<FileSystemNode>, targetDirectory: File, classPath: Array<File>) = try {
      tryToCompile(sourceDirectories, targetDirectory, classPath)

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
      val location = Location(e.location.fileName, e.location.lineNumber.toLong(), e.location.columnNumber.toLong())

      throw BuildException(Severity.ERROR, TAG, location, message, e)
   }

   private fun tryToCompile(sourceDirectories: Array<FileSystemNode>, targetDirectory: File, classPath: Array<File>) {
      val compilationUnits = sourceDirectories.flatMap(FileSystemNode::allChildren)
         .filter { it.type == FileType.FILE }
         .map(::FileNodeResourceAdapter)
         .toTypedArray()

      compilerFactory.newCompiler().apply {
         setDestinationDirectory(targetDirectory, false)

         setClassPath(classPath)

         setWarningHandler { handle, message, loc ->
            val location = Location(loc.fileName, loc.lineNumber.toLong(), loc.columnNumber.toLong())
            eventbus.fire(AppendBuildLogsEvent(Severity.WARNING, "$message ($handle)", location, TAG))
         }

         compile(compilationUnits)
      }
   }

   companion object {
      private const val TAG = "Compiler"
   }
}
