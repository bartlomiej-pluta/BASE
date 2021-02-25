package com.bartlomiejpluta.base.editor.code.compiler

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode
import java.io.File

interface ScriptCompiler {
   fun compile(sourceDirectory: FileSystemNode, targetDirectory: File)
}