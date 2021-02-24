package com.bartlomiejpluta.base.editor.code.compiler

import com.bartlomiejpluta.base.editor.code.model.FileSystemNode

interface ScriptCompiler {
   fun compile(sourceDirectory: FileSystemNode)
}