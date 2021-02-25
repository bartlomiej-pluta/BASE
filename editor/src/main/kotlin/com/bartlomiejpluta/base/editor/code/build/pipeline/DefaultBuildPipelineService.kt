package com.bartlomiejpluta.base.editor.code.build.pipeline

import com.bartlomiejpluta.base.editor.code.build.compiler.ScriptCompiler
import com.bartlomiejpluta.base.editor.code.build.game.GameEngineProvider
import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.code.build.project.ProjectAssembler
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultBuildPipelineService : BuildPipelineService {

   @Autowired
   private lateinit var compiler: ScriptCompiler

   @Autowired
   private lateinit var packager: JarPackager

   @Autowired
   private lateinit var engineProvider: GameEngineProvider

   @Autowired
   private lateinit var projectAssembler: ProjectAssembler

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun build() {
      projectContext.project?.let {
         prepareBuildDirectory(it)

         val outputFile = File(it.buildOutDirectory, OUTPUT_JAR_NAME)

         compiler.compile(it.codeFSNode, it.buildClassesDirectory)
         engineProvider.provideBaseGameEngine(outputFile)
         packager.pack(it.buildClassesDirectory, outputFile, "BOOT-INF/classes")
         projectAssembler.assembly(it, outputFile)
      }
   }

   private fun prepareBuildDirectory(project: Project) {
      project.buildDirectory.delete()

      project.buildClassesDirectory.mkdirs()
      project.buildOutDirectory.mkdirs()
   }

   companion object {
      private const val OUTPUT_JAR_NAME = "game.jar"
   }
}