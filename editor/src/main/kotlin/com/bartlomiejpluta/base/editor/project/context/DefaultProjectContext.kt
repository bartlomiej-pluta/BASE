package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.serial.MapSerializer
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.serial.ProjectDeserializer
import com.bartlomiejpluta.base.editor.project.serial.ProjectSerializer
import com.bartlomiejpluta.base.editor.util.uid.UID
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.getValue
import tornadofx.setValue
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Component
class DefaultProjectContext : ProjectContext {

   @Autowired
   private lateinit var projectSerializer: ProjectSerializer

   @Autowired
   private lateinit var projectDeserializer: ProjectDeserializer

   @Autowired
   private lateinit var mapSerializer: MapSerializer

   override val projectProperty = SimpleObjectProperty<Project?>() as ObjectProperty<Project?>
   override var project by projectProperty

   override fun save() {
      project?.let {
         it.sourceDirectory.mkdirs()

         FileOutputStream(File(it.sourceDirectory, "project.bep")).use { fos ->
            projectSerializer.serialize(it, fos)
         }
      }
   }

   override fun open(file: File) {
      FileInputStream(file)
         .use { projectDeserializer.deserialize(it) }
         .apply { sourceDirectoryProperty.value = file.parentFile }
         .let { project = it }
   }

   override fun attachMap(map: GameMap) {
      project?.let {
         UID.next(it.maps).let { uid ->
            map.uid = uid
            it.maps += uid
         }

         saveMap(it, map)
         save()
      }
   }

   private fun saveMap(project: Project, map: GameMap) {
      val dir = File(project.sourceDirectory, "maps")
      dir.mkdirs()
      File(dir, "${map.uid}.dat").outputStream().use { mapSerializer.serialize(map, it) }
   }
}