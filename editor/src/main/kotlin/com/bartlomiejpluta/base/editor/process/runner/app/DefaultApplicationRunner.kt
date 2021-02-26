package com.bartlomiejpluta.base.editor.process.runner.app

import com.bartlomiejpluta.base.editor.code.build.pipeline.BuildPipelineService
import com.bartlomiejpluta.base.editor.process.runner.jar.JarRunner
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.event.EventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.getValue
import tornadofx.runAsync
import tornadofx.setValue
import tornadofx.toProperty

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

   override fun run() {
      projectContext.project?.let { project ->
         if (isRunning) {
            return@let
         }

         isRunning = true
         pipelineService.build().onSucceeded = EventHandler { runApplication(project) }
      }
   }

   private fun runApplication(project: Project) {
      val builder = jarRunner.run(project.buildOutputJarFile)
      val process = builder.start()

      runAsync {
         process.inputStream.bufferedReader().forEachLine {
            println("OUTPUT -> $it")
         }
      }

      runAsync {
         process.errorStream.bufferedReader().forEachLine {
            println("ERROR -> $it")
         }
      }

      process.onExit().thenApply {
         println("EXIT WITH ${it.exitValue()}")
         isRunning = false
      }
   }

}