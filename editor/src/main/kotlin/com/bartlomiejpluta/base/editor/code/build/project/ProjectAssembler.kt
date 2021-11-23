package com.bartlomiejpluta.base.editor.code.build.project

import com.bartlomiejpluta.base.editor.project.model.Project
import java.io.File
import java.io.PrintStream

interface ProjectAssembler {
   fun assembly(project: Project, targetJar: File, stdout: PrintStream, stderr: PrintStream)
}