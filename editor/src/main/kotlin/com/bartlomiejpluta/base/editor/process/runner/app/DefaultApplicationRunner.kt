package com.bartlomiejpluta.base.editor.process.runner.app

import com.bartlomiejpluta.base.editor.code.build.pipeline.BuildPipelineService
import com.bartlomiejpluta.base.editor.common.logs.enumeration.Severity
import com.bartlomiejpluta.base.editor.event.AppendProcessLogsEvent
import com.bartlomiejpluta.base.editor.event.ClearProcessLogsEvent
import com.bartlomiejpluta.base.editor.process.runner.jar.JarRunner
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.beans.property.SimpleObjectProperty
import javafx.event.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.*
import tornadofx.FX.Companion.eventbus

@Component
class DefaultApplicationRunner : ApplicationRunner {

   @Autowired
   private lateinit var projectContext: ProjectContext

   @Autowired
   private lateinit var pipelineService: BuildPipelineService

   @Autowired
   private lateinit var jarRunner: JarRunner

   override val isRunningProperty = false.toProperty()
   private var isRunning by isRunningProperty

   override val processProperty = SimpleObjectProperty<Process>()
   private var process by processProperty

   override fun run() {
      projectContext.project?.let { project ->
         if (process != null) {
            return@let
         }

         isRunning = true

         if (project.buildOutputJarFile.exists() && project.buildOutputJarFile.isFile) {
            runApplication(project)
         } else {
            pipelineService.build().onSucceeded = EventHandler { runApplication(project) }
         }
      }
   }

   override fun terminate() {
      process?.destroyForcibly()
   }

   private fun runApplication(project: Project) {
      eventbus.fire(ClearProcessLogsEvent)

      val builder = jarRunner.run(project.buildOutputJarFile)
      process = builder.start()

      runAsync {
         process.inputStream.bufferedReader().forEachLine {
            eventbus.fire(AppendProcessLogsEvent(Severity.INFO, it))
         }
      }

      runAsync {
         process.errorStream.bufferedReader().forEachLine {
            eventbus.fire(AppendProcessLogsEvent(Severity.ERROR, it))
         }
      }

      process.onExit().thenApply {
         eventbus.fire(AppendProcessLogsEvent(Severity.INFO, "\nProcess exited with code ${it.exitValue()}"))
         process = null
         isRunning = false
      }
   }
}