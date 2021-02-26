package com.bartlomiejpluta.base.editor.process.runner.jar

import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultJarRunner : JarRunner {
   private val javaHome = System.getProperty("java.home")
   private val classpath = System.getProperty("java.class.path")
   private val javaBin = "$javaHome${FS}bin${FS}java" // == $JAVA_HOME/bin/java

   override fun run(jarFile: File, jvmArgs: Array<String>, args: Array<String>) = listOf(
      javaBin, *jvmArgs, "-cp", classpath, "-jar", jarFile.absolutePath, *args
   ).let { ProcessBuilder(it) }

   companion object {
      private val FS = File.separator
   }
}