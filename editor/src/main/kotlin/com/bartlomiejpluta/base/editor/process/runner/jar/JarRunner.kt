package com.bartlomiejpluta.base.editor.process.runner.jar

import java.io.File

interface JarRunner {
   fun run(jarFile: File, jvmArgs: Array<String> = emptyArray(), args: Array<String> = emptyArray()): ProcessBuilder
}