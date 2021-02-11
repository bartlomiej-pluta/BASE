package com.bartlomiejpluta.base.editor.project.viewmodel

import com.bartlomiejpluta.base.editor.project.model.Project
import tornadofx.*

class ProjectVM(project: Project) : ItemViewModel<Project>(project) {
   val nameProperty = bind(Project::nameProperty)
   val name by nameProperty

   val sourceDirectoryProperty = bind(Project::sourceDirectoryProperty)
   val sourceDirectory by sourceDirectoryProperty

   val maps = bind(Project::maps)
}