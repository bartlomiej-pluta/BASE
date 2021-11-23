package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import java.io.File
import java.io.PrintStream

interface Compiler {
   fun compile(
      sourceDirectories: Array<FileSystemNode>,
      targetDirectory: File,
      classPath: Array<File> = emptyArray(),
      stdout: PrintStream,
      stderr: PrintStream
   )
}