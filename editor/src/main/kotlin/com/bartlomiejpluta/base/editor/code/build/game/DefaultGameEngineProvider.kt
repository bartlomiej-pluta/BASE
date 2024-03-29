package com.bartlomiejpluta.base.editor.code.build.game

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import org.springframework.stereotype.Component
import java.io.File
import java.io.PrintStream

/* TODO
 * There is an idea to have a different GameEngine providers for different OS (Windows, Mac OS X, Linux etc.)
 * Essentially as far as whole system is built on Java, which is platform-independent, the only thing that
 * actually is platform dependent is the game engine which is built on top of the LWJGL, which is in turn built
 * on the top of the OpenGL driver. So potentially this is the only place that would be duplicated in order
 * to provide a true multi-platform support. Of course the :editor application would have to have a proper
 * engines provided to the resources then, however this is a job for the Gradle build system.
 */

@Component
class DefaultGameEngineProvider : GameEngineProvider {

   override fun provideBaseGameEngine(targetJar: File, stdout: PrintStream, stderr: PrintStream) {
      try {
         tryToProvide(targetJar)
      } catch (e: Exception) {
         stderr.println("[$TAG] ${e.message}")
         throw BuildException()
      }
   }

   private fun tryToProvide(targetJar: File) {
      javaClass.getResourceAsStream(GAME_ENGINE_JAR).use { ris ->
         targetJar.outputStream().use { fos ->
            ris?.copyTo(fos)
         }
      }
   }

   companion object {
      private const val GAME_ENGINE_JAR = "/engine/engine.jar"
      private const val TAG = "Engine Provider"
   }
}