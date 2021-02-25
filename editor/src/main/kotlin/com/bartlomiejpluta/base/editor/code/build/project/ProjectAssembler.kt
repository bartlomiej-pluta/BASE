package com.bartlomiejpluta.base.editor.code.build.project

import com.bartlomiejpluta.base.editor.project.model.Project
import java.io.File

interface ProjectAssembler {
   fun assembly(project: Project, targetJar: File)
}