package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.code.build.model.ClasspathResource
import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import com.bartlomiejpluta.base.editor.event.UpdateCompilationLogEvent
import org.codehaus.commons.compiler.CompileException
import org.codehaus.commons.compiler.util.resource.FileResource
import org.codehaus.commons.compiler.util.resource.Resource
import org.codehaus.janino.CompilerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import tornadofx.FX
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors.toList

@Component
class JaninoCompiler : ScriptCompiler {
   private val compilerFactory = CompilerFactory()

   @Value("classpath:api/**/*.java")
   private lateinit var apiFiles: Array<org.springframework.core.io.Resource>

   override fun compile(sourceDirectory: FileSystemNode, targetDirectory: File) {
      val sources = sourceDirectory
         .allChildren
         .map(FileSystemNode::file)
         .filter(File::isFile)
         .map(::FileResource)
         .toTypedArray<Resource>()

      val api = apiFiles
         .map(::ClasspathResource)
         .toTypedArray()

      val resources = sources + api

      val compiler = compilerFactory.newCompiler()


      // FIXME:
      // For some reason the compiler's error handler does not want to catch
      // syntax errors. The only way to catch it is just catching CompileExceptions
      try {
         compiler.compile(resources)
         FX.eventbus.fire(UpdateCompilationLogEvent(UpdateCompilationLogEvent.Severity.INFO, "Compilation success"))
         moveClassFilesToTargetDirectory(sourceDirectory.file, targetDirectory)
      } catch (e: CompileException) {

         // Because the Janino compiler assemblies the message with the location
         // in the LocatedException.getMessage() method, we just need to remove it
         // to have a plain message along with the plain location as separated objects
         val locationIndex = e.location?.toString()?.length ?: 0
         val message = e.message?.substring(locationIndex) ?: ""
         FX.eventbus.fire(UpdateCompilationLogEvent(UpdateCompilationLogEvent.Severity.ERROR, message, e.location))
      }
   }

   private fun moveClassFilesToTargetDirectory(sourceDirectory: File, targetDirectory: File) {
      val files = Files.walk(sourceDirectory.toPath())
         .filter(Files::isRegularFile)
         .map(Path::toFile)
         .collect(toList())

      files.filter { it.extension == "class" }.forEach {
         val relative = it.toRelativeString(sourceDirectory)
         val targetFile = File(targetDirectory, relative)
         it.copyTo(targetFile, overwrite = true)
         it.delete()
      }
   }
}
