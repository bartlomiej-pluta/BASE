package com.bartlomiejpluta.base.editor.code.build.project

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.project.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.PrintStream

@Component
class DefaultProjectAssembler : ProjectAssembler {

   @Autowired
   private lateinit var packager: JarPackager

   override fun assembly(project: Project, targetJar: File, stdout: PrintStream, stderr: PrintStream) {
      try {
         tryToAssembly(project, targetJar)
      } catch (e: Exception) {
         stderr.println("[$TAG] ${e.message}")
         throw BuildException()
      }
   }

   private fun tryToAssembly(project: Project, targetJar: File) {
      packager.pack(project.mapsDirectory, targetJar, "BOOT-INF/classes/project/maps")
      packager.pack(project.tileSetsDirectory, targetJar, "BOOT-INF/classes/project/tilesets")
      packager.pack(project.imagesDirectory, targetJar, "BOOT-INF/classes/project/images")
      packager.pack(project.characterSetsDirectory, targetJar, "BOOT-INF/classes/project/charsets")
      packager.pack(project.animationsDirectory, targetJar, "BOOT-INF/classes/project/animations")
      packager.pack(project.iconSetsDirectory, targetJar, "BOOT-INF/classes/project/iconsets")
      packager.pack(project.fontsDirectory, targetJar, "BOOT-INF/classes/project/fonts")
      packager.pack(project.widgetsDirectory, targetJar, "BOOT-INF/classes/project/widgets")
      packager.pack(project.audioDirectory, targetJar, "BOOT-INF/classes/project/audio")
      packager.copy(project.projectFile, targetJar, "BOOT-INF/classes/project")
   }

   companion object {
      private const val TAG = "Project Assembler"
   }
}