package com.bartlomiejpluta.base.editor.project.viewmodel

import com.bartlomiejpluta.base.editor.project.model.Project
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue

class ProjectVM(project: Project) : ItemViewModel<Project>(project) {
   val nameProperty = bind(Project::nameProperty)
   var name by nameProperty
}