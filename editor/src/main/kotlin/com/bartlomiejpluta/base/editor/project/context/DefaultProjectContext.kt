package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.serial.MapDeserializer
import com.bartlomiejpluta.base.editor.map.serial.MapSerializer
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.serial.ProjectDeserializer
import com.bartlomiejpluta.base.editor.project.serial.ProjectSerializer
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetBuilder
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import com.bartlomiejpluta.base.editor.util.uid.UID
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import tornadofx.getValue
import tornadofx.setValue
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Component
class DefaultProjectContext : ProjectContext {
   private val tileSetCache = mutableMapOf<String, TileSet>()

   @Autowired
   private lateinit var projectSerializer: ProjectSerializer

   @Autowired
   private lateinit var projectDeserializer: ProjectDeserializer

   @Autowired
   private lateinit var mapSerializer: MapSerializer

   @Autowired
   private lateinit var mapDeserializer: MapDeserializer

   override val projectProperty = SimpleObjectProperty<Project?>() as ObjectProperty<Project?>
   override var project by projectProperty

   init {
      projectProperty.addListener { _, _, newProject ->
         newProject?.mkdirs()
         tileSetCache.clear()
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
            val asset = GameMapAsset(uid, name)
            map.uid = uid
            it.maps += asset

            save()
            File(it.mapsDirectory, asset.source).outputStream().use { fos -> mapSerializer.serialize(map, fos) }
         }
      }
   }

   override fun loadMap(uid: String) = project?.let {
      val asset = it.maps.firstOrNull { map -> map.uid == uid }
         ?: throw IllegalStateException("The map with uid [$uid] does not exist ")

      File(it.mapsDirectory, asset.source).inputStream().use { fis -> mapDeserializer.deserialize(fis) }
   } ?: throw IllegalStateException("There is no open project in the context")

   override fun saveMap(map: GameMap) {
      project?.let {
         val asset = it.maps.firstOrNull { asset -> asset.uid == map.uid }
            ?: throw IllegalStateException("The map with uid [${map.uid}] does not exist ")

         File(it.mapsDirectory, asset.source).outputStream().use { fos -> mapSerializer.serialize(map, fos) }
      }
   }

   override fun importTileSet(builder: TileSetAssetBuilder) {
      project?.let {
         UID.next(it.tileSets.map(Asset::uid)).let { uid ->
            val source = "$uid.${builder.file.extension}"
            val targetFile = File(it.tileSetsDirectory, source)
            builder.file.copyTo(targetFile)
            it.tileSets += TileSetAsset(uid, source, builder.name, builder.rows, builder.columns)

            save()
         }
      }
   }

   override fun loadTileSet(uid: String) = tileSetCache.getOrPut(uid) {
      project?.let {
         val asset = it.tileSets.firstOrNull { tileSet -> tileSet.uid == uid }
            ?: throw IllegalStateException("The Tile Set with uid [$uid] does not exist ")

         val image = File(it.tileSetsDirectory, asset.source).inputStream().use { fis -> Image(fis) }

         TileSet(uid, asset.name, image, asset.rows, asset.columns)
      } ?: throw IllegalStateException("There is no open project in the context")
   }
}