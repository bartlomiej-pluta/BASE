package com.bartlomiejpluta.base.editor.project.manager

import com.bartlomiejpluta.base.editor.project.model.Project
import java.io.File

interface ProjectManager {
   fun saveProject(project: Project)
   fun openProject(file: File): Project
}