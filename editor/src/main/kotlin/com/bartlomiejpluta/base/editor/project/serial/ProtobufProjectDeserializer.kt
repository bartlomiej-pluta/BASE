package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAsset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAsset
import com.bartlomiejpluta.base.editor.entityset.asset.EntitySet
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
class ProtobufProjectDeserializer : ProjectDeserializer {

   override fun deserialize(input: InputStream): Project {
      val proto = ProjectProto.Project.parseFrom(input)

      return Project().apply {
         name = proto.name
         runner = proto.runner
         maps.addAll(proto.mapsList.map { deserializeMap(this, it) })
         tileSets.addAll(proto.tileSetsList.map { deserializeTileSet(this, it) })
         images.addAll(proto.imagesList.map { deserializeImage(this, it) })
         entitySets.addAll(proto.entitySetsList.map { deserializeEntitySet(this, it) })
         animations.addAll(proto.animationsList.map { deserializeAnimation(this, it) })
         iconSets.addAll(proto.iconSetsList.map { deserializeIconSet(this, it) })
         fonts.addAll(proto.fontsList.map { deserializeFont(this, it) })
         widgets.addAll(proto.widgetsList.map { deserializeWidget(this, it) })
         sounds.addAll(proto.soundsList.map { deserializeSound(this, it) })
      }
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

   private fun deserializeImage(project: Project, image: ProjectProto.ImageAsset) = ImageAsset(
      project = project,
      uid = image.uid,
      source = image.source,
      name = image.name
   )

   private fun deserializeEntitySet(project: Project, entitySetAsset: ProjectProto.EntitySetAsset) = EntitySet(
      project = project,
      uid = entitySetAsset.uid,
      source = entitySetAsset.source,
      name = entitySetAsset.name,
      rows = entitySetAsset.rows,
      columns = entitySetAsset.columns
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
