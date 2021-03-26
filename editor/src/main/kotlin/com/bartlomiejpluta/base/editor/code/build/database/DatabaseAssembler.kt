package com.bartlomiejpluta.base.editor.code.build.database

import com.bartlomiejpluta.base.editor.project.model.Project
import java.io.File

interface DatabaseAssembler {
   fun assembly(project: Project, targetJar: File)
}