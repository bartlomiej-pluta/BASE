package com.bartlomiejpluta.base.editor.project.viewmodel

import com.bartlomiejpluta.base.editor.project.model.Project
import tornadofx.ItemViewModel

class ProjectVM(project: Project) : ItemViewModel<Project>(project) {
   val nameProperty = bind(Project::nameProperty)
   val sourceDirectoryProperty = bind(Project::sourceDirectoryProperty)
   val projectFileProperty = bind(Project::projectFileProperty)
   val runnerProperty = bind(Project::runnerProperty)
   val mapsDirectoryProperty = bind(Project::mapsDirectoryProperty)
   val tileSetsDirectoryProperty = bind(Project::tileSetsDirectoryProperty)
   val imagesDirectoryProperty = bind(Project::imagesDirectoryProperty)
   val codeDirectoryProperty = bind(Project::codeDirectoryProperty)
   val codeFSNodeProperty = bind(Project::codeFSNodeProperty)
   val buildDirectoryProperty = bind(Project::buildDirectoryProperty)
   val buildClassesDirectoryProperty = bind(Project::buildClassesDirectoryProperty)
   val buildOutDirectoryProperty = bind(Project::buildOutDirectoryProperty)
   val buildOutputJarFileProperty = bind(Project::buildOutputJarFileProperty)
}