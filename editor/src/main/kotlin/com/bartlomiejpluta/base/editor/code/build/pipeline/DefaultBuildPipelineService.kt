package com.bartlomiejpluta.base.editor.code.build.pipeline

import com.bartlomiejpluta.base.editor.code.build.compiler.Compiler
import com.bartlomiejpluta.base.editor.code.build.database.DatabaseAssembler
import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.game.GameEngineProvider
import com.bartlomiejpluta.base.editor.code.build.generator.CodeGenerator
import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.code.build.project.ProjectAssembler
import com.bartlomiejpluta.base.editor.code.dependency.DependenciesProvider
import com.bartlomiejpluta.base.editor.event.ClearBuildLogsEvent
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import com.bartlomiejpluta.base.editor.project.model.Project
import javafx.beans.property.SimpleObjectProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.*
import tornadofx.FX.Companion.eventbus
import java.io.OutputStream
import java.io.PrintStream

@Component
class DefaultBuildPipelineService : BuildPipelineService {

   @Autowired
   private lateinit var dependenciesProvider: DependenciesProvider

   @Autowired
   private lateinit var generators: List<CodeGenerator>

   @Autowired
   private lateinit var compiler: Compiler

   @Autowired
   private lateinit var packager: JarPackager

   @Autowired
   private lateinit var engineProvider: GameEngineProvider

   @Autowired
   private lateinit var projectAssembler: ProjectAssembler

   @Autowired
   private lateinit var databaseAssembler: DatabaseAssembler

   @Autowired
   private lateinit var projectContext: ProjectContext

   private val latchProperty = SimpleObjectProperty<Latch?>()
   private var latch by latchProperty

   private lateinit var stdout: OutputStream
   private lateinit var stderr: OutputStream
   private lateinit var out: PrintStream
   private lateinit var err: PrintStream

   override fun initStreams(stdout: OutputStream, stderr: OutputStream) {
      this.stdout = stdout
      this.stderr = stderr
      this.out = PrintStream(stdout)
      this.err = PrintStream(stderr)
   }

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
         cancel()
         return@runAsync false
      }

      latch = Latch()

      val startTime = System.currentTimeMillis()

      try {
         projectContext.project?.let(this@DefaultBuildPipelineService::runPipeline)
         out.println("Build completed")
         return@runAsync true
      } catch (e: BuildException) {
         err.println("Build failed")
      } finally {
         latch?.release()
         val buildingTime = (System.currentTimeMillis() - startTime) / 1000.0
         out.println("Finished in [${buildingTime}s]")
      }

      false
   }

   private fun runPipeline(project: Project) {
      eventbus.fire(ClearBuildLogsEvent)
      prepareBuildDirectory(project)

      val outputFile = project.buildOutputJarFile

      out.println("Providing compile-time dependencies...")
      val dependencies = dependenciesProvider.provideDependenciesTo(project.buildDependenciesDirectory)

      out.println("Generating sources...")
      generators.forEach(CodeGenerator::generate)

      out.println("Compiling sources...")
      compiler.compile(
         arrayOf(project.codeFSNode, FileSystemNode(project.buildGeneratedCodeDirectory)),
         project.buildClassesDirectory,
         dependencies.toTypedArray(),
         out,
         err
      )

      out.println("Assembling game engine...")
      engineProvider.provideBaseGameEngine(outputFile, out, err)

      out.println("Linking compilation units...")
      packager.pack(project.buildClassesDirectory, outputFile, "BOOT-INF/classes")

      out.println("Assembling project assets...")
      projectAssembler.assembly(project, outputFile, out, err)

      out.println("Assembling database...")
      databaseAssembler.assembly(project, outputFile, out, err)
   }

   private fun prepareBuildDirectory(project: Project) {
      project.buildDirectory.deleteRecursively()

      project.buildClassesDirectory.mkdirs()
      project.buildOutDirectory.mkdirs()
      project.buildDependenciesDirectory.mkdirs()
      project.buildGeneratedCodeDirectory.mkdirs()
   }

   override fun clean() {
      projectContext.project?.apply { buildDirectory.deleteRecursively() }
      out.println("Cleaning done")
   }
}