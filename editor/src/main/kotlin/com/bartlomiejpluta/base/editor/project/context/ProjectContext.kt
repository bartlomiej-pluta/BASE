package com.bartlomiejpluta.base.editor.project.context

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAssetData
import com.bartlomiejpluta.base.editor.asset.model.Asset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAssetData
import com.bartlomiejpluta.base.editor.code.model.Code
import com.bartlomiejpluta.base.editor.characterset.asset.CharacterSetAssetData
import com.bartlomiejpluta.base.editor.file.model.FileNode
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAssetData
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAssetData
import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAssetData
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
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
   fun createNewProject(project: Project)

   fun importMap(name: String, map: GameMap)
   fun importMapFromFile(name: String, handler: String, file: File, tileSet: TileSet): GameMap
   fun loadMap(uid: String): GameMap
   fun saveMap(map: GameMap)

   fun importTileSet(data: TileSetAssetData)
   fun loadTileSet(uid: String): TileSet

   fun importImage(data: ImageAssetData)
   fun findImageAsset(uid: String): ImageAsset
   fun loadImage(uid: String): Image

   fun importCharacterSet(data: CharacterSetAssetData)

   fun importAnimation(data: AnimationAssetData)

   fun importIconSet(data: IconSetAssetData)

   fun importFont(data: FontAssetData)

   fun createWidget(data: WidgetAssetData): WidgetAsset

   fun importSound(data: SoundAssetData)
   fun deleteAsset(asset: Asset)
   fun loadScript(fileNode: FileNode, execute: ((String) -> Unit)?, saveable: Boolean): Code
   fun saveScript(code: Code)
}