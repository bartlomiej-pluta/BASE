package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.image.asset.ImageAssetData
import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetData
import com.bartlomiejpluta.base.editor.tileset.model.TileSet
import javafx.beans.property.ObjectProperty
import javafx.scene.image.Image
import java.io.File

interface ProjectContext {
   val projectProperty: ObjectProperty<Project?>
   var project: Project?

   fun save()
   fun open(file: File)

   fun importMap(name: String, map: GameMap)
   fun loadMap(uid: String): GameMap
   fun saveMap(map: GameMap)

   fun importTileSet(data: TileSetAssetData)
   fun loadTileSet(uid: String): TileSet

   fun importImage(data: ImageAssetData)
   fun loadImage(uid: String): Image
}