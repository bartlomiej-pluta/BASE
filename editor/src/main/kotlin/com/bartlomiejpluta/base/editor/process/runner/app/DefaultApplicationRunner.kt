package com.bartlomiejpluta.base.editor.process.runner.app

import com.bartlomiejpluta.base.editor.code.build.pipeline.BuildPipelineService
import com.bartlomiejpluta.base.editor.event.ClearProcessLogsEvent
import com.bartlomiejpluta.base.editor.process.runner.jar.JarRunner
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.beans.property.SimpleObjectProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.FX.Companion.eventbus
import tornadofx.getValue
import tornadofx.runAsync
import tornadofx.setValue
import tornadofx.toProperty
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream

@Component
class DefaultApplicationRunner : ApplicationRunner {

   @Autowired
   private lateinit var projectContext: ProjectContext

   @Autowired
   private lateinit var pipelineService: BuildPipelineService

   @Autowired
   private lateinit var jarRunner: JarRunner

   private lateinit var stdout: OutputStream
   private lateinit var stderr: OutputStream
   private lateinit var out: PrintStream
   private lateinit var err: PrintStream

   override val isRunningProperty = false.toProperty()

   private var isRunning by isRunningProperty
   override val processProperty = SimpleObjectProperty<Process>()

   private var process by processProperty

   override fun initStreams(stdout: OutputStream, stderr: OutputStream) {
      this.stdout = stdout
      this.stderr = stderr
      this.out = PrintStream(stdout)
      this.err = PrintStream(stderr)
   }

   override fun run() {
      projectContext.project?.let { project ->
         if (process != null) {
            return@let
         }

         isRunning = true

         if (project.buildOutputJarFile.exists() && project.buildOutputJarFile.isFile) {
            runApplication(project)
         } else {
            val build = pipelineService.build()
            build.setOnSucceeded {
               if (build.value) {
                  runApplication(project)
               } else {
                  isRunning = false
               }
            }
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
         try {
            process.inputStream.transferTo(stdout)
            process.errorStream.transferTo(stderr)
         } catch (e: IOException) {
            // Ignore stream termination exception
         }
      }

      process.onExit().thenApply {
         out.println("\nProcess exited with code ${it.exitValue()}")
         process = null
         isRunning = false
      }
   }
}