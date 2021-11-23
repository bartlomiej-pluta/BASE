package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.file.model.FileType
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.io.File
import java.io.PrintStream
import java.io.PrintWriter
import javax.tools.Diagnostic
import javax.tools.DiagnosticCollector
import javax.tools.JavaFileObject
import javax.tools.ToolProvider

@Primary
@Component
class JDKCompiler : Compiler {
   private val compiler = ToolProvider.getSystemJavaCompiler()

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun compile(
      sourceDirectories: Array<FileSystemNode>,
      targetDirectory: File,
      classPath: Array<File>,
      stdout: PrintStream,
      stderr: PrintStream
   ) {
      val classpath = classPath.joinToString(":") { it.absolutePath }
      val options = listOf("-g", "-d", targetDirectory.absolutePath, "-classpath", classpath)

      val collector = DiagnosticCollector<JavaFileObject>()

      val manager = compiler.getStandardFileManager(collector, null, null)
      val sources = sourceDirectories.flatMap(FileSystemNode::allChildren)
         .filter { it.type == FileType.FILE }
         .mapNotNull { it as? FileSystemNode }
         .map { it.file }
         .let { manager.getJavaFileObjects(*it.toTypedArray()) }

      val task = compiler.getTask(PrintWriter(stdout), manager, collector, options, null, sources)

      val success = task.call()

      collector.diagnostics.forEach(compilationError(stdout, stderr))

      if (!success) {
         throw BuildException()
      }
   }

   private fun compilationError(stdout: PrintStream, stderr: PrintStream): (Diagnostic<out JavaFileObject>) -> Unit {

      return { d ->
         val stream = when (d.kind!!) {
            Diagnostic.Kind.ERROR -> stderr
            Diagnostic.Kind.WARNING -> stderr
            Diagnostic.Kind.MANDATORY_WARNING -> stderr
            Diagnostic.Kind.NOTE -> stdout
            Diagnostic.Kind.OTHER -> stdout
         }

         val source =
            projectContext.project?.let { File(d.source.name).toRelativeString(it.codeFSNode.file) } ?: d.source.name

         stream.println("[$TAG] $source:${d.lineNumber},${d.columnNumber}: ${d.getMessage(null)}")
      }
   }

   companion object {
      private const val TAG = "Compiler"
   }
}