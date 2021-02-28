package com.bartlomiejpluta.base.editor.code.build.compiler

import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import java.io.File

interface Compiler {
   fun compile(sourceDirectory: FileSystemNode, targetDirectory: File)
}