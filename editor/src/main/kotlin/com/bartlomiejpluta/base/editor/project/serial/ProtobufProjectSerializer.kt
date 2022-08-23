package com.bartlomiejpluta.base.editor.project.serial

import com.bartlomiejpluta.base.editor.animation.asset.AnimationAsset
import com.bartlomiejpluta.base.editor.audio.asset.SoundAsset
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
import java.io.OutputStream

@Component
class ProtobufProjectSerializer : ProjectSerializer {

   override fun serialize(item: Project, output: OutputStream) {
      val proto = ProjectProto.Project.newBuilder()
      proto.name = item.name
      proto.runner = item.runner
      proto.addAllMaps(item.maps.map(this::serializeMap))
      proto.addAllTileSets(item.tileSets.map(this::serializeTileSet))
      proto.addAllImages(item.images.map(this::serializeImage))
      proto.addAllCharacterSets(item.characterSets.map(this::serializeCharacterSet))
      proto.addAllAnimations(item.animations.map(this::serializeAnimation))
      proto.addAllIconSets(item.iconSets.map(this::serializeIconSet))
      proto.addAllFonts(item.fonts.map(this::serializeFont))
      proto.addAllWidgets(item.widgets.map(this::serializeWidget))
      proto.addAllSounds(item.sounds.map(this::serializeSound))
      proto.build().writeTo(output)
   }

   private fun serializeMap(map: GameMapAsset) = ProjectProto.GameMapAsset.newBuilder()
      .setUid(map.uid)
      .setSource(map.source)
      .setName(map.name)
      .build()

   private fun serializeTileSet(tileSet: TileSetAsset) = ProjectProto.TileSetAsset.newBuilder()
      .setUid(tileSet.uid)
      .setSource(tileSet.source)
      .setName(tileSet.name)
      .setRows(tileSet.rows)
      .setColumns(tileSet.columns)
      .build()

   private fun serializeImage(image: ImageAsset) = ProjectProto.ImageAsset.newBuilder()
      .setUid(image.uid)
      .setSource(image.source)
      .setName(image.name)
      .build()

   private fun serializeCharacterSet(characterSet: CharacterSetAsset) = ProjectProto.CharacterSetAsset.newBuilder()
      .setUid(characterSet.uid)
      .setSource(characterSet.source)
      .setName(characterSet.name)
      .setRows(characterSet.rows)
      .setColumns(characterSet.columns)
      .build()

   private fun serializeAnimation(animation: AnimationAsset) = ProjectProto.AnimationAsset.newBuilder()
      .setUid(animation.uid)
      .setSource(animation.source)
      .setName(animation.name)
      .setRows(animation.rows)
      .setColumns(animation.columns)
      .build()

   private fun serializeIconSet(iconSet: IconSetAsset) = ProjectProto.IconSetAsset.newBuilder()
      .setUid(iconSet.uid)
      .setSource(iconSet.source)
      .setName(iconSet.name)
      .setRows(iconSet.rows)
      .setColumns(iconSet.columns)
      .build()

   private fun serializeFont(font: FontAsset) = ProjectProto.FontAsset.newBuilder()
      .setUid(font.uid)
      .setSource(font.source)
      .setName(font.name)
      .build()

   private fun serializeWidget(widget: WidgetAsset) = ProjectProto.WidgetAsset.newBuilder()
      .setUid(widget.uid)
      .setSource(widget.source)
      .setName(widget.name)
      .build()

   private fun serializeSound(sound: SoundAsset) = ProjectProto.SoundAsset.newBuilder()
      .setUid(sound.uid)
      .setSource(sound.source)
      .setName(sound.name)
      .build()
}
