package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.map.model.map.GameMap
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAssetBuilder
import javafx.beans.property.ObjectProperty
import java.io.File

interface ProjectContext {
   val projectProperty: ObjectProperty<Project?>
   var project: Project?

   fun save()
   fun open(file: File)

   fun importMap(name: String, map: GameMap)
   fun loadMap(uid: String): GameMap

   fun importTileSet(builder: TileSetAssetBuilder)
}