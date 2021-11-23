package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.model.FileNodeResourceAdapter
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import org.codehaus.commons.compiler.CompileException
import org.codehaus.janino.CompilerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.PrintStream

@Component
class JaninoCompiler : Compiler {
   private val compilerFactory = CompilerFactory()

   override fun compile(
      sourceDirectories: Array<FileSystemNode>,
      targetDirectory: File,
      classPath: Array<File>,
      stdout: PrintStream,
      stderr: PrintStream
   ) = try {
      tryToCompile(sourceDirectories, targetDirectory, classPath, stdout)

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
      stderr.println("[$TAG] ${e.message}")
      throw BuildException()
   }

   private fun tryToCompile(
      sourceDirectories: Array<FileSystemNode>,
      targetDirectory: File,
      classPath: Array<File>,
      stdout: PrintStream
   ) {
      val compilationUnits = sourceDirectories.flatMap(FileSystemNode::allChildren)
         .filter { it.type == FileType.FILE }
         .map(::FileNodeResourceAdapter)
         .toTypedArray()

      compilerFactory.newCompiler().apply {
         setDestinationDirectory(targetDirectory, false)

         setClassPath(classPath)

         setWarningHandler { handle, message, loc ->
            stdout.println("[$TAG] $message:$loc ($handle)")
         }

         compile(compilationUnits)
      }
   }

   companion object {
      private const val TAG = "Compiler"
   }
}
