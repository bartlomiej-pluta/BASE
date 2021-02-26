package com.bartlomiejpluta.base.editor.code.build.pipeline

import com.bartlomiejpluta.base.editor.code.build.compiler.Compiler
import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.game.GameEngineProvider
import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.code.build.project.ProjectAssembler
import com.bartlomiejpluta.base.editor.event.AppendCompilationLogEvent
import com.bartlomiejpluta.base.editor.event.AppendCompilationLogEvent.Severity.INFO
import com.bartlomiejpluta.base.editor.event.ClearCompilationLogEvent
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.FX.Companion.eventbus
import tornadofx.runAsync
import java.io.File

@Component
class DefaultBuildPipelineService : BuildPipelineService {

   @Autowired
   private lateinit var compiler: Compiler

   @Autowired
   private lateinit var packager: JarPackager

   @Autowired
   private lateinit var engineProvider: GameEngineProvider

   @Autowired
   private lateinit var projectAssembler: ProjectAssembler

   @Autowired
   private lateinit var projectContext: ProjectContext

   // FIXME
   // There is runAsync used right here, however it does not prevent from
   // multiple pipeline running. There should be some kind of assertion
   // that enforces the pipeline to run only once in the same time.
   override fun build() = runAsync {
      try {
         projectContext.project?.let(this@DefaultBuildPipelineService::runPipeline)
      } catch (e: BuildException) {
         val event = AppendCompilationLogEvent(e.severity, e.message, e.location, e.tag)
         eventbus.fire(event)
      }

      Unit
   }

   private fun runPipeline(project: Project) {
      eventbus.fire(ClearCompilationLogEvent)
      prepareBuildDirectory(project)

      val outputFile = File(project.buildOutDirectory, OUTPUT_JAR_NAME)
      val startTime = System.currentTimeMillis()

      eventbus.fire(AppendCompilationLogEvent(INFO, "Compiling sources...", tag = TAG))
      compiler.compile(project.codeFSNode, project.buildClassesDirectory)

      eventbus.fire(AppendCompilationLogEvent(INFO, "Assembling game engine...", tag = TAG))
      engineProvider.provideBaseGameEngine(outputFile)

      eventbus.fire(AppendCompilationLogEvent(INFO, "Assembling project assets...", tag = TAG))
      packager.pack(project.buildClassesDirectory, outputFile, "BOOT-INF/classes")
      projectAssembler.assembly(project, outputFile)

      val buildingTime = (System.currentTimeMillis() - startTime) / 1000.0
      eventbus.fire(AppendCompilationLogEvent(INFO, "Done [${buildingTime}s]", tag = TAG))
   }

   private fun prepareBuildDirectory(project: Project) {
      project.buildDirectory.delete()

      project.buildClassesDirectory.mkdirs()
      project.buildOutDirectory.mkdirs()
   }

   companion object {
      private const val OUTPUT_JAR_NAME = "game.jar"
      private const val TAG = "Build"
   }
}