package com.bartlomiejpluta.base.editor.code.compiler

import com.bartlomiejpluta.base.editor.project.context.ProjectContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DefaultCompilingService : CompilingService {

   @Autowired
   private lateinit var compiler: ScriptCompiler

   @Autowired
   private lateinit var projectContext: ProjectContext

   override fun compile() {
      projectContext.project?.let {
         compiler.compile(it.codeFSNode, it.buildClassesDirectory)
      }
   }
}