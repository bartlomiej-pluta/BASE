package com.bartlomiejpluta.base.editor.project.model

import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.context.DefaultProjectContext
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

   val mapsDirectoryProperty = SimpleObjectProperty<File>()
   var mapsDirectory by mapsDirectoryProperty
      private set

   val tileSetsDirectoryProperty = SimpleObjectProperty<File>()
   var tileSetsDirectory by tileSetsDirectoryProperty

   init {
      sourceDirectoryProperty.addListener { _, _, dir ->
         dir?.let {
            mapsDirectory = File(it, MAPS_DIR)
            tileSetsDirectory = File(it, TILESETS_DIR)
         }
      }
   }

   fun mkdirs() {
      sourceDirectory?.mkdirs()
      mapsDirectory?.mkdirs()
      tileSetsDirectory?.mkdirs()
   }

   companion object {
      const val MAPS_DIR = "maps"
      const val TILESETS_DIR = "tilesets"
   }
}
