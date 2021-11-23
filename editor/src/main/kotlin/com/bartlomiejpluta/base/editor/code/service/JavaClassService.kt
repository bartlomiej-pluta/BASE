package com.bartlomiejpluta.base.editor.code.service

import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import freemarker.template.Version
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Path
import java.util.*

@Component
class JavaClassService {

   @Autowired
   private lateinit var projectContext: ProjectContext

   private val config = Configuration(Version(2, 3, 20)).apply {
      defaultEncoding = "UTF-8"
      locale = Locale.US
      templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
      setClassForTemplateLoading(this.javaClass, "/java_templates/")
   }

   fun toPathString(className: String) = toPath(className).toString() + ".java"
   fun toPath(className: String) = Path.of("", *className.split(".").toTypedArray())

   fun ofPath(path: String): String = ofPath(Path.of(path))
   fun ofPath(path: Path): String = path.joinToString(".")

   fun createClassFile(
      name: String,
      directory: FileSystemNode,
      classTemplate: String,
      inflate: (MutableMap<String, String>) -> Unit = {}
   ) {
      projectContext.project?.let { project ->
         val template = config.getTemplate(classTemplate)

         val className = name.substringAfterLast(".")
         val classFile = File(directory.file, toPath(name).toString() + ".java")
         val classPackage = ofPath(
            File(directory.file, toPath(name).toString()).parentFile.relativeTo(project.codeFSNode.file).toPath()
         )

         project.codeFSNode.createNode(classFile.toRelativeString(project.codeFSNode.file))

         val model = mutableMapOf("className" to className, "package" to classPackage).apply(inflate).toMap()
         template.process(model, classFile.writer())
      }
   }
}