package com.bartlomiejpluta.base.editor.project.model

import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import javafx.beans.binding.Bindings.createObjectBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.observableListOf
import tornadofx.setValue
import java.io.File

class Project {
   val nameProperty = SimpleStringProperty()
   var name by nameProperty

   val sourceDirectoryProperty = SimpleObjectProperty<File>()
   val sourceDirectory by sourceDirectoryProperty

   val projectFileProperty = createObjectBinding({ File(sourceDirectory, PROJECT_FILE) }, sourceDirectoryProperty)
   val projectFile by projectFileProperty

   val runnerProperty = SimpleStringProperty()
   var runner by runnerProperty

   val maps = observableListOf<GameMapAsset>()
   val tileSets = observableListOf<TileSetAsset>()
   val images = observableListOf<ImageAsset>()

   val assetLists = listOf(maps, tileSets, images)

   val mapsDirectoryProperty = SimpleObjectProperty<File>()
   var mapsDirectory by mapsDirectoryProperty
      private set

   val tileSetsDirectoryProperty = SimpleObjectProperty<File>()
   var tileSetsDirectory by tileSetsDirectoryProperty
      private set

   val imagesDirectoryProperty = SimpleObjectProperty<File>()
   var imagesDirectory by imagesDirectoryProperty
      private set

   val codeDirectoryProperty = SimpleObjectProperty<File>()
   var codeDirectory by codeDirectoryProperty
      private set
   val codeFSNodeProperty = createObjectBinding({ FileSystemNode(codeDirectory) }, codeDirectoryProperty)
   val codeFSNode by codeFSNodeProperty

   // Build directories
   val buildDirectoryProperty = SimpleObjectProperty<File>()
   var buildDirectory by buildDirectoryProperty
      private set

   val buildClassesDirectoryProperty = SimpleObjectProperty<File>()
   var buildClassesDirectory by buildClassesDirectoryProperty
      private set

   val buildOutDirectoryProperty = SimpleObjectProperty<File>()
   var buildOutDirectory by buildOutDirectoryProperty
      private set

   val buildOutputJarFileProperty =
      createObjectBinding({ File(buildOutDirectory, PROJECT_OUTPUT_JAR_FILE) }, buildOutDirectoryProperty)
   val buildOutputJarFile by buildOutputJarFileProperty

   init {
      sourceDirectoryProperty.addListener { _, _, dir ->
         dir?.let {
            mapsDirectory = File(it, MAPS_DIR)
            tileSetsDirectory = File(it, TILESETS_DIR)
            imagesDirectory = File(it, IMAGES_DIR)
            codeDirectory = File(it, CODE_DIR)
            buildDirectory = File(it, BUILD_DIR)
            buildClassesDirectory = File(it, BUILD_CLASSES_DIR)
            buildOutDirectory = File(it, BUILD_OUT_DIR)
         }
      }
   }

   fun mkdirs() {
      sourceDirectory?.mkdirs()
      mapsDirectory?.mkdirs()
      tileSetsDirectory?.mkdirs()
      imagesDirectory?.mkdirs()
      codeDirectory?.mkdirs()
   }

   companion object {
      const val PROJECT_FILE = "project.bep"
      const val PROJECT_OUTPUT_JAR_FILE = "game.jar"

      const val MAPS_DIR = "maps"
      const val TILESETS_DIR = "tilesets"
      const val IMAGES_DIR = "images"
      const val CODE_DIR = "code"
      const val BUILD_DIR = "build"
      const val BUILD_CLASSES_DIR = "$BUILD_DIR/classes"
      const val BUILD_OUT_DIR = "$BUILD_DIR/out"
   }
}
