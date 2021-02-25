package com.bartlomiejpluta.base.editor.code.build.project

import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.project.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File

@Component
class DefaultProjectAssembler : ProjectAssembler {

   @Autowired
   private lateinit var packager: JarPackager

   override fun assembly(project: Project, targetJar: File) {
      packager.pack(project.mapsDirectory, targetJar, "BOOT-INF/classes/project/maps")
      packager.pack(project.tileSetsDirectory, targetJar, "BOOT-INF/classes/project/tilesets")
      packager.pack(project.imagesDirectory, targetJar, "BOOT-INF/classes/project/images")
      packager.copy(project.projectFile, targetJar, "BOOT-INF/classes/project")
   }
}