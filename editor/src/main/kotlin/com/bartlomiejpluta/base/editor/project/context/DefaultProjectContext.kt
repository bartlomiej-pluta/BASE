package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.serial.MapSerializer
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.serial.ProjectDeserializer
import com.bartlomiejpluta.base.editor.project.serial.ProjectSerializer
import com.bartlomiejpluta.base.editor.util.uid.UID
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleObjectProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.getValue
import tornadofx.select
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

   private val mapsDirectoryProperty = SimpleObjectProperty<File?>()
   private val mapsDirectory by mapsDirectoryProperty

   init {
      projectProperty.addListener { _, _, newProject ->
         when(newProject) {
            null -> {
               mapsDirectoryProperty.value = null
            }

            else -> {
               mapsDirectoryProperty.value = File(newProject.sourceDirectory, MAPS_DIR).apply(File::mkdirs)
            }
         }
      }
   }

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

   override fun importMap(name: String, map: GameMap) {
      project?.let {
         UID.next(it.maps.map(Asset::uid)).let { uid ->
            val asset = GameMapAsset(uid, name, map.rows, map.columns)
            map.uid = uid
            it.maps += asset

            save()
            File(mapsDirectory, asset.source).outputStream().use { fos -> mapSerializer.serialize(map, fos) }
         }
      }
   }

   companion object {
      const val MAPS_DIR = "maps"
   }
}