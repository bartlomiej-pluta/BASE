package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAsset
import com.bartlomiejpluta.base.editor.animation.asset.AnimationAssetData
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAsset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAssetData
import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAsset
import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAssetData
import com.bartlomiejpluta.base.editor.characterset.asset.CharacterSetAsset
import com.bartlomiejpluta.base.editor.characterset.asset.CharacterSetAssetData
import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.code.model.CodeType
import com.bartlomiejpluta.base.editor.code.service.JavaClassService
import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAsset
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAssetData
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAssetData
import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAsset
import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAssetData
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.image.asset.ImageAssetData
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.map.serial.MapDeserializer
import com.bartlomiejpluta.base.editor.map.serial.MapSerializer
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.project.serial.ProjectDeserializer
import com.bartlomiejpluta.base.editor.project.serial.ProjectSerializer
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetData
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import com.bartlomiejpluta.base.editor.util.uid.UID
import javafx.beans.binding.Bindings.createObjectBinding
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
import java.util.*

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

   @Autowired
   private lateinit var javaClassService: JavaClassService

   override val projectProperty = SimpleObjectProperty<Project?>() as ObjectProperty<Project?>
   override var project by projectProperty

   init {
      projectProperty.addListener { _, prevProject, newProject ->
         prevProject?.dispose()
         newProject?.init()
         tileSetCache.clear()
      }
   }

   override fun save() {
      project?.let {
         it.sourceDirectory.mkdirs()

         FileOutputStream(it.projectFile).use { fos ->
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

   override fun createNewProject(project: Project) {
      this.project = project
      save()

      javaClassService.createClassFile(project.runner, project.codeFSNode, "game_runner.ftl")
   }

   override fun importMap(name: String, map: GameMap) {
      project?.let {
         UID.next(it.maps.map(Asset::uid)).let { uid ->
            val asset = GameMapAsset(it, uid, name)
            map.uid = uid
            it.maps += asset

            save()

            javaClassService.createClassFile(map.handler, it.codeFSNode, "map_handler.ftl") { model ->
               model["mapUid"] = uid
               model["mapName"] = name
               model["mapCode"] = name.split("\\s+".toRegex()).joinToString("") { part ->
                  part.replaceFirstChar { c ->
                     if (c.isLowerCase()) c.titlecase(Locale.getDefault())
                     else c.toString()
                  }
               }
               model["map_code"] = name.split("\\s+".toRegex()).joinToString("_") { part -> part.lowercase() }
            }

            File(it.mapsDirectory, asset.source).outputStream().use { fos -> mapSerializer.serialize(map, fos) }
         }
      }
   }

   override fun importMapFromFile(
      name: String,
      handler: String,
      file: File,
      replaceTileSet: (String, String) -> String
   ) =
      project?.let { project ->
         val map = file.inputStream().use { mapDeserializer.deserialize(it, replaceTileSet) }
         UID.next(project.maps.map(Asset::uid)).let { uid ->
            val asset = GameMapAsset(project, uid, name)
            map.uid = uid
            project.maps += asset

            save()

            javaClassService.createClassFile(handler, project.codeFSNode, "map_handler.ftl") { model ->
               model["mapUid"] = uid
            }

            File(project.mapsDirectory, asset.source).outputStream().use { fos -> mapSerializer.serialize(map, fos) }

            map
         }
      } ?: throw IllegalStateException("There is no open project in the context")

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

   override fun importTileSet(data: TileSetAssetData) {
      project?.let {
         UID.next(it.tileSets.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.tileSetsDirectory, source)
            data.file.copyTo(targetFile)
            it.tileSets += TileSetAsset(it, uid, source, data.name, data.rows, data.columns)

            save()
         }
      }
   }

   override fun importAutoTile(data: AutoTileAssetData) {
      project?.let {
         UID.next(it.autoTiles.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.autoTilesDirectory, source)
            data.file.copyTo(targetFile)
            it.autoTiles += AutoTileAsset(it, uid, source, data.name)

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

   override fun findTileSetAsset(uid: String) = project?.let {
      it.tileSets.firstOrNull { tileSets -> tileSets.uid == uid }
         ?: throw IllegalStateException("The Tile Set with uid [$uid] does not exist ")
   } ?: throw IllegalStateException("There is no open project in the context")

   override fun importImage(data: ImageAssetData) {
      project?.let {
         UID.next(it.images.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.imagesDirectory, source)
            data.file.copyTo(targetFile)
            it.images += ImageAsset(it, uid, source, data.name)

            save()
         }
      }
   }

   override fun findImageAsset(uid: String) = project?.let {
      it.images.firstOrNull { image -> image.uid == uid }
         ?: throw IllegalStateException("The Image with uid [$uid] does not exist ")
   } ?: throw IllegalStateException("There is no open project in the context")

   override fun loadImage(uid: String) = project?.let {
      val asset = it.images.firstOrNull { image -> image.uid == uid }
         ?: throw IllegalStateException("The Image with uid [$uid] does not exist ")

      File(it.imagesDirectory, asset.source).inputStream().use { fis -> Image(fis) }
   } ?: throw IllegalStateException("There is no open project in the context")

   override fun importCharacterSet(data: CharacterSetAssetData) {
      project?.let {
         UID.next(it.characterSets.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.characterSetsDirectory, source)
            data.file.copyTo(targetFile)
            it.characterSets += CharacterSetAsset(it, uid, source, data.name, data.rows, data.columns)

            save()
         }
      }
   }

   override fun importAnimation(data: AnimationAssetData) {
      project?.let {
         UID.next(it.animations.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.animationsDirectory, source)
            data.file.copyTo(targetFile)
            it.animations += AnimationAsset(it, uid, source, data.name, data.rows, data.columns)

            save()
         }
      }
   }

   override fun importIconSet(data: IconSetAssetData) {
      project?.let {
         UID.next(it.iconSets.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.iconSetsDirectory, source)
            data.file.copyTo(targetFile)
            it.iconSets += IconSetAsset(it, uid, source, data.name, data.rows, data.columns)

            save()
         }
      }
   }

   override fun importFont(data: FontAssetData) {
      project?.let {
         UID.next(it.fonts.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.fontsDirectory, source)
            data.file.copyTo(targetFile)
            it.fonts += FontAsset(it, uid, source, data.name)

            save()
         }
      }
   }

   override fun createWidget(data: WidgetAssetData) = project?.let {
      UID.next(it.widgets.map(Asset::uid)).let { uid ->
         val asset = WidgetAsset(it, uid, data.name)
         val file = File(it.widgetsDirectory, asset.source)
         file.createNewFile()
         it.widgets += asset

         save()

         asset
      }
   } ?: throw IllegalStateException("There is no open project in the context")

   override fun importSound(data: SoundAssetData) {
      project?.let {
         UID.next(it.sounds.map(Asset::uid)).let { uid ->
            val source = "$uid.${data.file.extension}"
            val targetFile = File(it.audioDirectory, source)
            data.file.copyTo(targetFile)
            it.sounds += SoundAsset(it, uid, source, data.name)

            save()
         }
      }
   }

   override fun deleteAsset(asset: Asset) {
      project?.let {
         it.assetLists.firstOrNull { assets -> assets.remove(asset) }
            ?: throw IllegalStateException("The asset does not belong to any of the assets lists")

         asset.file.delete()
      } ?: throw IllegalStateException("There is no open project in the context")
   }

   override fun loadScript(fileNode: FileNode, execute: ((String) -> Unit)?, saveable: Boolean): Code {
      val typeProperty = SimpleObjectProperty<CodeType>().apply {
         bind(createObjectBinding({
            when (fileNode.extension.lowercase(Locale.getDefault())) {
               "java" -> CodeType.JAVA
               "xml" -> CodeType.XML
               "sql" -> CodeType.SQL
               else -> throw IllegalStateException("Unsupported script type")
            }
         }))
      }


      val code = fileNode.readText()

      return Code(fileNode, typeProperty, code, saveable, execute)
   }

   override fun saveScript(code: Code) {
      code.fileNode.writeText(code.code)
   }
}