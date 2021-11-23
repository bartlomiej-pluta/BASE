package com.bartlomiejpluta.base.editor.code.build.database

import com.bartlomiejpluta.base.editor.project.model.Project
import java.io.File
import java.io.PrintStream

interface DatabaseAssembler {
   fun assembly(project: Project, targetJar: File, stdout: PrintStream, stderr: PrintStream)
}