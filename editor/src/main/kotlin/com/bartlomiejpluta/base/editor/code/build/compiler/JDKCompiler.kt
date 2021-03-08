package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.common.logs.model.Location
import com.bartlomiejpluta.base.editor.event.AppendBuildLogsEvent
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import tornadofx.FX
import java.io.File
import java.util.*
import javax.tools.Diagnostic
import javax.tools.DiagnosticCollector
import javax.tools.JavaFileObject
import javax.tools.ToolProvider

@Primary
@Component
class JDKCompiler : Compiler {
   private val compiler = ToolProvider.getSystemJavaCompiler()

   override fun compile(sourceDirectory: FileSystemNode, targetDirectory: File, classPath: Array<File>) {
      val classpath = classPath.joinToString(";") { it.absolutePath }
      val options = listOf("-g", "-d", targetDirectory.absolutePath, "-classpath", classpath)

      val collector = DiagnosticCollector<JavaFileObject>()

      val manager = compiler.getStandardFileManager(collector, null, null)
      val sources = sourceDirectory.allChildren
         .filter { it.type == FileType.FILE }
         .mapNotNull { it as? FileSystemNode }
         .map { it.file }
         .let { manager.getJavaFileObjects(*it.toTypedArray()) }

      val task = compiler.getTask(null, manager, collector, options, null, sources)

      val success = task.call()

      collector.diagnostics.forEach(this::fireDiagnosticEvent)

      if (!success) {
         throw BuildException()
      }
   }

   private fun fireDiagnosticEvent(diagnostic: Diagnostic<out JavaFileObject>) {
      val severity = when (diagnostic.kind!!) {
         Diagnostic.Kind.ERROR -> Severity.ERROR
         Diagnostic.Kind.WARNING -> Severity.WARNING
         Diagnostic.Kind.MANDATORY_WARNING -> Severity.WARNING
         Diagnostic.Kind.NOTE -> Severity.NOTE
         Diagnostic.Kind.OTHER -> Severity.INFO
      }

      val location = Location(diagnostic.source.name, diagnostic.lineNumber, diagnostic.columnNumber)


      FX.eventbus.fire(AppendBuildLogsEvent(severity, diagnostic.getMessage(null), location, TAG))
   }

   companion object {
      private const val TAG = "Compiler"
   }
}