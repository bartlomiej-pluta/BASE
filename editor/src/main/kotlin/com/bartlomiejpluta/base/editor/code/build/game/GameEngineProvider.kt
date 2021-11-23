package com.bartlomiejpluta.base.editor.code.build.game

import java.io.File
import java.io.PrintStream

interface GameEngineProvider {
   fun provideBaseGameEngine(targetJar: File, stdout: PrintStream, stderr: PrintStream)
}