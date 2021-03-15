package com.bartlomiejpluta.base.editor.project.model

import com.bartlomiejpluta.base.editor.entityset.asset.EntitySet
import com.bartlomiejpluta.base.editor.file.model.FileSystemNode
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
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
   val entitySets = observableListOf<EntitySet>()
   val fonts = observableListOf<FontAsset>()
   val widgets = observableListOf<WidgetAsset>()

   val assetLists = listOf(maps, tileSets, images, entitySets, fonts, widgets)

   val mapsDirectoryProperty = SimpleObjectProperty<File>()
   var mapsDirectory by mapsDirectoryProperty
      private set

   val tileSetsDirectoryProperty = SimpleObjectProperty<File>()
   var tileSetsDirectory by tileSetsDirectoryProperty
      private set

   val imagesDirectoryProperty = SimpleObjectProperty<File>()
   var imagesDirectory by imagesDirectoryProperty
      private set

   val entitySetsDirectoryProperty = SimpleObjectProperty<File>()
   var entitySetsDirectory by entitySetsDirectoryProperty
      private set

   val fontsDirectoryProperty = SimpleObjectProperty<File>()
   var fontsDirectory by fontsDirectoryProperty
      private set

   val widgetsDirectoryProperty = SimpleObjectProperty<File>()
   var widgetsDirectory by widgetsDirectoryProperty
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
            tileSetsDirectory = File(it, TILE_SETS_DIR)
            imagesDirectory = File(it, IMAGES_DIR)
            entitySetsDirectory = File(it, ENTITY_SETS_DIR)
            fontsDirectory = File(it, FONTS_DIR)
            widgetsDirectory = File(it, WIDGETS_DIR)
            codeDirectory = File(it, CODE_DIR)
            buildDirectory = File(it, BUILD_DIR)
            buildClassesDirectory = File(it, BUILD_CLASSES_DIR)
            buildDependenciesDirectory = File(it, BUILD_DEPENDENCIES_DIR)
            buildOutDirectory = File(it, BUILD_OUT_DIR)
         }
      }
   }

   fun mkdirs() {
      sourceDirectory?.mkdirs()
      mapsDirectory?.mkdirs()
      tileSetsDirectory?.mkdirs()
      imagesDirectory?.mkdirs()
      entitySetsDirectory?.mkdirs()
      fontsDirectory?.mkdirs()
      widgetsDirectory?.mkdirs()
      codeDirectory?.mkdirs()
   }

   companion object {
      const val PROJECT_FILE = "project.bep"
      const val PROJECT_OUTPUT_JAR_FILE = "game.jar"

      const val MAPS_DIR = "maps"
      const val TILE_SETS_DIR = "tilesets"
      const val IMAGES_DIR = "images"
      const val ENTITY_SETS_DIR = "entsets"
      const val FONTS_DIR = "fonts"
      const val WIDGETS_DIR = "widgets"
      const val CODE_DIR = "code"
      const val BUILD_DIR = "build"
      const val BUILD_CLASSES_DIR = "$BUILD_DIR/classes"
      const val BUILD_OUT_DIR = "$BUILD_DIR/out"
      const val BUILD_DEPENDENCIES_DIR = "$BUILD_DIR/dependencies"
   }
}
