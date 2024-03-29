package com.bartlomiejpluta.base.editor.project.model

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAsset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAsset
import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAsset
import com.bartlomiejpluta.base.editor.database.source.H2DBDataSource
import com.bartlomiejpluta.base.editor.characterset.asset.CharacterSetAsset
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAsset
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

   val databaseFileProperty = createObjectBinding({ File(sourceDirectory, DATABASE_FILE) }, sourceDirectoryProperty)
   val databaseFile by databaseFileProperty

   val runnerProperty = SimpleStringProperty()
   var runner by runnerProperty

   val maps = observableListOf<GameMapAsset>()
   val tileSets = observableListOf<TileSetAsset>()
   val autoTiles = observableListOf<AutoTileAsset>()
   val images = observableListOf<ImageAsset>()
   val characterSets = observableListOf<CharacterSetAsset>()
   val animations = observableListOf<AnimationAsset>()
   val iconSets = observableListOf<IconSetAsset>()
   val fonts = observableListOf<FontAsset>()
   val widgets = observableListOf<WidgetAsset>()
   val sounds = observableListOf<SoundAsset>()

   val assetLists = listOf(maps, tileSets, autoTiles, images, characterSets, animations, iconSets, fonts, widgets, sounds)

   val mapsDirectoryProperty = SimpleObjectProperty<File>()
   var mapsDirectory by mapsDirectoryProperty
      private set

   val tileSetsDirectoryProperty = SimpleObjectProperty<File>()
   var tileSetsDirectory by tileSetsDirectoryProperty
      private set

   val autoTilesDirectoryProperty = SimpleObjectProperty<File>()
   var autoTilesDirectory by autoTilesDirectoryProperty
      private set

   val imagesDirectoryProperty = SimpleObjectProperty<File>()
   var imagesDirectory by imagesDirectoryProperty
      private set

   val characterSetsDirectoryProperty = SimpleObjectProperty<File>()
   var characterSetsDirectory by characterSetsDirectoryProperty
      private set

   val animationsDirectoryProperty = SimpleObjectProperty<File>()
   var animationsDirectory by animationsDirectoryProperty
      private set

   val iconSetsDirectoryProperty = SimpleObjectProperty<File>()
   var iconSetsDirectory by iconSetsDirectoryProperty
      private set

   val fontsDirectoryProperty = SimpleObjectProperty<File>()
   var fontsDirectory by fontsDirectoryProperty
      private set

   val widgetsDirectoryProperty = SimpleObjectProperty<File>()
   var widgetsDirectory by widgetsDirectoryProperty
      private set

   val audioDirectoryProperty = SimpleObjectProperty<File>()
   var audioDirectory by audioDirectoryProperty
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

   val buildDependenciesDirectoryProperty = SimpleObjectProperty<File>()
   var buildDependenciesDirectory by buildDependenciesDirectoryProperty
      private set

   val buildDatabaseDumpDirectoryProperty = SimpleObjectProperty<File>()
   var buildDatabaseDumpDirectory by buildDatabaseDumpDirectoryProperty
      private set

   val buildOutDirectoryProperty = SimpleObjectProperty<File>()
   var buildOutDirectory by buildOutDirectoryProperty
      private set

   val buildGeneratedCodeDirectoryProperty = SimpleObjectProperty<File>()
   var buildGeneratedCodeDirectory by buildGeneratedCodeDirectoryProperty
      private set

   val buildDatabaseDumpFileProperty =
      createObjectBinding({ File(buildDatabaseDumpDirectory, DATABASE_DUMP_FILE) }, buildDatabaseDumpDirectoryProperty)
   val buildDatabaseDumpFile by buildDatabaseDumpFileProperty

   val buildOutputJarFileProperty =
      createObjectBinding({ File(buildOutDirectory, PROJECT_OUTPUT_JAR_FILE) }, buildOutDirectoryProperty)
   val buildOutputJarFile by buildOutputJarFileProperty

   val buildAssetsDirProperty = SimpleObjectProperty<File>()
   var buildAssetsDir by buildAssetsDirProperty
      private set

   val binaryProjectFileProperty = createObjectBinding({ File(buildAssetsDir, BINARY_PROJECT_FILE) }, buildAssetsDirProperty)
   val binaryProjectFile by binaryProjectFileProperty

   val buildAssetsMapsDirProperty = createObjectBinding({ File(buildAssetsDir, MAPS_DIR) }, buildAssetsDirProperty)
   val buildAssetsMapsDir by buildAssetsMapsDirProperty

   lateinit var database: H2DBDataSource

   init {
      sourceDirectoryProperty.addListener { _, _, dir ->
         dir?.let {
            mapsDirectory = File(it, MAPS_DIR)
            tileSetsDirectory = File(it, TILE_SETS_DIR)
            autoTilesDirectory = File(it, AUTO_TILES)
            imagesDirectory = File(it, IMAGES_DIR)
            characterSetsDirectory = File(it, CHARACTER_SETS_DIR)
            animationsDirectory = File(it, ANIMATIONS_DIR)
            iconSetsDirectory = File(it, ICONSETS_DIR)
            fontsDirectory = File(it, FONTS_DIR)
            widgetsDirectory = File(it, WIDGETS_DIR)
            audioDirectory = File(it, AUDIO_DIR)
            codeDirectory = File(it, CODE_DIR)
            buildDirectory = File(it, BUILD_DIR)
            buildClassesDirectory = File(it, BUILD_CLASSES_DIR)
            buildDependenciesDirectory = File(it, BUILD_DEPENDENCIES_DIR)
            buildGeneratedCodeDirectory = File(it, BUILD_GENERATED_DIR)
            buildAssetsDir = File(it, BUILD_ASSETS_DIR)
            buildDatabaseDumpDirectory = File(it, BUILD_DATABASE_DUMP_DIR)
            buildOutDirectory = File(it, BUILD_OUT_DIR)
         }
      }
   }

   fun init() {
      database = H2DBDataSource(databaseFile)
      mkdirs()
   }

   fun dispose() {
      database.close()
   }

   private fun mkdirs() {
      sourceDirectory?.mkdirs()
      mapsDirectory?.mkdirs()
      tileSetsDirectory?.mkdirs()
      autoTilesDirectory?.mkdirs()
      imagesDirectory?.mkdirs()
      characterSetsDirectory?.mkdirs()
      animationsDirectory?.mkdirs()
      iconSetsDirectory?.mkdirs()
      fontsDirectory?.mkdirs()
      widgetsDirectory?.mkdirs()
      audioDirectory?.mkdirs()
      codeDirectory?.mkdirs()
   }

   companion object {
      const val PROJECT_FILE = "project.json"
      const val BINARY_PROJECT_FILE = "project.bep"
      const val DATABASE_FILE = "data.sql"
      const val DATABASE_DUMP_FILE = "data.sql"
      const val PROJECT_OUTPUT_JAR_FILE = "game.jar"

      const val MAPS_DIR = "maps"
      const val TILE_SETS_DIR = "tilesets"
      const val AUTO_TILES = "autotiles"
      const val IMAGES_DIR = "images"
      const val CHARACTER_SETS_DIR = "charsets"
      const val ANIMATIONS_DIR = "animations"
      const val ICONSETS_DIR = "iconsets"
      const val FONTS_DIR = "fonts"
      const val WIDGETS_DIR = "widgets"
      const val AUDIO_DIR = "audio"
      const val CODE_DIR = "src/main/java"
      const val BUILD_DIR = "build"
      const val BUILD_CLASSES_DIR = "$BUILD_DIR/classes"
      const val BUILD_OUT_DIR = "$BUILD_DIR/out"
      const val BUILD_DEPENDENCIES_DIR = "$BUILD_DIR/dependencies"
      const val BUILD_GENERATED_DIR = "$BUILD_DIR/generated"
      const val BUILD_ASSETS_DIR = "$BUILD_DIR/assets"
      const val BUILD_DATABASE_DUMP_DIR = "$BUILD_DIR/db"
   }
}
