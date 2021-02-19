package com.bartlomiejpluta.base.editor.project.model

import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
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

   val maps = observableListOf<GameMapAsset>()
   val tileSets = observableListOf<TileSetAsset>()
   val images = observableListOf<ImageAsset>()

   val mapsDirectoryProperty = SimpleObjectProperty<File>()
   var mapsDirectory by mapsDirectoryProperty
      private set

   val tileSetsDirectoryProperty = SimpleObjectProperty<File>()
   var tileSetsDirectory by tileSetsDirectoryProperty
      private set

   val imagesDirectoryProperty = SimpleObjectProperty<File>()
   var imagesDirectory by imagesDirectoryProperty
      private set

   init {
      sourceDirectoryProperty.addListener { _, _, dir ->
         dir?.let {
            mapsDirectory = File(it, MAPS_DIR)
            tileSetsDirectory = File(it, TILESETS_DIR)
            imagesDirectory = File(it, IMAGES_DIR)
         }
      }
   }

   fun mkdirs() {
      sourceDirectory?.mkdirs()
      mapsDirectory?.mkdirs()
      tileSetsDirectory?.mkdirs()
      imagesDirectory?.mkdirs()
   }

   companion object {
      const val MAPS_DIR = "maps"
      const val TILESETS_DIR = "tilesets"
      const val IMAGES_DIR = "images"
   }
}
