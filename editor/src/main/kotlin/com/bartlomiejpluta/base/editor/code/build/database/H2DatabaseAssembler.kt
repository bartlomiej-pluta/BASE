package com.bartlomiejpluta.base.editor.code.build.database

import com.bartlomiejpluta.base.editor.code.build.exception.BuildException
import com.bartlomiejpluta.base.editor.code.build.packager.JarPackager
import com.bartlomiejpluta.base.editor.database.service.DatabaseService
import com.bartlomiejpluta.base.editor.project.model.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.PrintStream

@Component
class H2DatabaseAssembler : DatabaseAssembler {

   @Autowired
   private lateinit var databaseService: DatabaseService

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
      val dump = project.buildDatabaseDumpFile
      dumpDatabase(dump)
      copyDumpToTargetJar(dump, targetJar)
   }

   private fun dumpDatabase(dumpFile: File) {
      databaseService.run {
         val statement = prepareStatement("SCRIPT TO ?")
         statement.setString(1, dumpFile.absolutePath)
         statement.execute()
      }
   }

   private fun copyDumpToTargetJar(dumpFile: File, targetJar: File) {
      packager.copy(dumpFile, targetJar, "BOOT-INF/classes/database")
   }

   companion object {
      private const val TAG = "Database Assembler"
   }
}