package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAsset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAsset
import com.bartlomiejpluta.base.editor.autotile.asset.AutoTileAsset
import com.bartlomiejpluta.base.editor.autotile.model.AutoTileLayout
import com.bartlomiejpluta.base.editor.characterset.asset.CharacterSetAsset
import com.bartlomiejpluta.base.editor.gui.font.asset.FontAsset
import com.bartlomiejpluta.base.editor.gui.widget.asset.WidgetAsset
import com.bartlomiejpluta.base.editor.iconset.asset.IconSetAsset
import com.bartlomiejpluta.base.editor.image.asset.ImageAsset
import com.bartlomiejpluta.base.editor.map.asset.GameMapAsset
import com.bartlomiejpluta.base.editor.project.model.Project
import com.bartlomiejpluta.base.editor.tileset.asset.TileSetAsset
import com.bartlomiejpluta.base.proto.ProjectProto
import org.springframework.stereotype.Component
import java.io.InputStream

@Component
open class ProtobufProjectDeserializer : BinaryProjectDeserializer {

   override fun deserialize(input: InputStream): Project = buildObject(ProjectProto.Project.parseFrom(input))

   fun buildObject(proto: ProjectProto.Project): Project = Project().apply {
      name = proto.name
      runner = proto.runner
      maps.addAll(proto.mapsList.map { deserializeMap(this, it) })
      tileSets.addAll(proto.tileSetsList.map { deserializeTileSet(this, it) })
      autoTiles.addAll(proto.autoTilesList.map { deserializeAutoTile(this, it) })
      images.addAll(proto.imagesList.map { deserializeImage(this, it) })
      characterSets.addAll(proto.characterSetsList.map { deserializeCharacterSet(this, it) })
      animations.addAll(proto.animationsList.map { deserializeAnimation(this, it) })
      iconSets.addAll(proto.iconSetsList.map { deserializeIconSet(this, it) })
      fonts.addAll(proto.fontsList.map { deserializeFont(this, it) })
      widgets.addAll(proto.widgetsList.map { deserializeWidget(this, it) })
      sounds.addAll(proto.soundsList.map { deserializeSound(this, it) })
   }

   private fun deserializeMap(project: Project, map: ProjectProto.GameMapAsset) = GameMapAsset(
      project = project,
      uid = map.uid,
      name = map.name
   )

   private fun deserializeTileSet(project: Project, tileSet: ProjectProto.TileSetAsset) = TileSetAsset(
      project = project,
      uid = tileSet.uid,
      source = tileSet.source,
      name = tileSet.name,
      rows = tileSet.rows,
      columns = tileSet.columns
   )

   private fun deserializeAutoTile(project: Project, autoTile: ProjectProto.AutoTileSetAsset) = AutoTileAsset(
      project = project,
      uid = autoTile.uid,
      source = autoTile.source,
      name = autoTile.name,
      rows = autoTile.rows,
      columns = autoTile.columns,
      layout = AutoTileLayout.valueOf(autoTile.layout.name)
   )

   private fun deserializeImage(project: Project, image: ProjectProto.ImageAsset) = ImageAsset(
      project = project,
      uid = image.uid,
      source = image.source,
      name = image.name
   )

   private fun deserializeCharacterSet(project: Project, characterSetAsset: ProjectProto.CharacterSetAsset) = CharacterSetAsset(
      project = project,
      uid = characterSetAsset.uid,
      source = characterSetAsset.source,
      name = characterSetAsset.name,
      rows = characterSetAsset.rows,
      columns = characterSetAsset.columns
   )

   private fun deserializeAnimation(project: Project, animationAsset: ProjectProto.AnimationAsset) = AnimationAsset(
      project = project,
      uid = animationAsset.uid,
      source = animationAsset.source,
      name = animationAsset.name,
      rows = animationAsset.rows,
      columns = animationAsset.columns
   )

   private fun deserializeIconSet(project: Project, iconSetAsset: ProjectProto.IconSetAsset) = IconSetAsset(
      project = project,
      uid = iconSetAsset.uid,
      source = iconSetAsset.source,
      name = iconSetAsset.name,
      rows = iconSetAsset.rows,
      columns = iconSetAsset.columns
   )

   private fun deserializeFont(project: Project, fontAsset: ProjectProto.FontAsset) = FontAsset(
      project = project,
      uid = fontAsset.uid,
      source = fontAsset.source,
      name = fontAsset.name
   )

   private fun deserializeWidget(project: Project, widget: ProjectProto.WidgetAsset) = WidgetAsset(
      project = project,
      uid = widget.uid,
      name = widget.name
   )

   private fun deserializeSound(project: Project, sound: ProjectProto.SoundAsset) = SoundAsset(
      project = project,
      uid = sound.uid,
      source = sound.source,
      name = sound.name
   )
}
