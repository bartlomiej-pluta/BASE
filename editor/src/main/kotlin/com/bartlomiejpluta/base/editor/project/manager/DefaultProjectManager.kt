package com.bartlomiejpluta.base.editor.project.manager

import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.serial.ProjectDeserializer
import com.bartlomiejpluta.base.editor.project.serial.ProjectSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Component
class DefaultProjectManager : ProjectManager {

   @Autowired
   private lateinit var projectSerializer: ProjectSerializer

   @Autowired
   private lateinit var projectDeserializer: ProjectDeserializer

   override fun saveProject(project: Project) {
      project.sourceDirectory.mkdirs()

      FileOutputStream(File(project.sourceDirectory, "project.bep")).use {
         projectSerializer.serialize(project, it)
      }
   }

   override fun openProject(file: File) = FileInputStream(file)
      .use { projectDeserializer.deserialize(it) }
      .apply { sourceDirectoryProperty.value = file.parentFile }
}