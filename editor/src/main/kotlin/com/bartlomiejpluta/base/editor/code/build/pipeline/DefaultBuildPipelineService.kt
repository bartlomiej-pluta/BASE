package com.bartlomiejpluta.base.editor.code.build.pipeline

import com.bartlomiejpluta.base.editor.code.build.compiler.Compiler
import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.game.GameEngineProvider
import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.code.build.project.ProjectAssembler
import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.event.AppendBuildLogsEvent
import com.bartlomiejpluta.base.editor.event.ClearBuildLogsEvent
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.beans.property.SimpleObjectProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.*
import tornadofx.FX.Companion.eventbus

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

   private val latchProperty = SimpleObjectProperty<Latch?>()
   private var latch by latchProperty

   override val isRunningProperty = false.toProperty()
   private var isRunning by isRunningProperty

   init {
      latchProperty.addListener { _, _, latch ->
         when (latch) {
            null -> isRunning = false
            else -> isRunningProperty.bind(latch.lockedProperty())
         }
      }
   }

   override fun build() = runAsync {
      latch?.locked?.takeIf { it }?.let {
         return@runAsync
      }

      latch = Latch()

      try {
         projectContext.project?.let(this@DefaultBuildPipelineService::runPipeline)
      } catch (e: BuildException) {
         val event = AppendBuildLogsEvent(e.severity, e.message, e.location, e.tag)
         eventbus.fire(event)
      } finally {
         latch?.release()
      }

      Unit
   }

   private fun runPipeline(project: Project) {
      eventbus.fire(ClearBuildLogsEvent)
      prepareBuildDirectory(project)

      val outputFile = project.buildOutputJarFile
      val startTime = System.currentTimeMillis()

      eventbus.fire(AppendBuildLogsEvent(Severity.INFO, "Compiling sources...", tag = TAG))
      compiler.compile(project.codeFSNode, project.buildClassesDirectory)

      eventbus.fire(AppendBuildLogsEvent(Severity.INFO, "Assembling game engine...", tag = TAG))
      engineProvider.provideBaseGameEngine(outputFile)

      eventbus.fire(AppendBuildLogsEvent(Severity.INFO, "Assembling project assets...", tag = TAG))
      packager.pack(project.buildClassesDirectory, outputFile, "BOOT-INF/classes")
      projectAssembler.assembly(project, outputFile)

      val buildingTime = (System.currentTimeMillis() - startTime) / 1000.0
      eventbus.fire(AppendBuildLogsEvent(Severity.INFO, "Done [${buildingTime}s]", tag = TAG))
   }

   private fun prepareBuildDirectory(project: Project) {
      project.buildDirectory.delete()

      project.buildClassesDirectory.mkdirs()
      project.buildOutDirectory.mkdirs()
   }

   companion object {
      private const val TAG = "Build"
   }
}