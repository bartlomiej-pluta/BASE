package com.bartlomiejpluta.base.engine.project.serial;

import com.bartlomiejpluta.base.engine.audio.asset.SoundAsset;
import com.bartlomiejpluta.base.engine.gui.asset.FontAsset;
import com.bartlomiejpluta.base.engine.gui.asset.WidgetDefinitionAsset;
import com.bartlomiejpluta.base.engine.project.model.Project;
import com.bartlomiejpluta.base.engine.world.animation.asset.AnimationAsset;
import com.bartlomiejpluta.base.engine.world.autotile.asset.AutoTileSetAsset;
import com.bartlomiejpluta.base.engine.world.character.asset.CharacterSetAsset;
import com.bartlomiejpluta.base.engine.world.icon.asset.IconSetAsset;
import com.bartlomiejpluta.base.engine.world.image.asset.ImageAsset;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.tileset.asset.TileSetAsset;
import com.bartlomiejpluta.base.proto.ProjectProto;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import static java.util.stream.Collectors.toList;

@Component
public class ProtobufProjectDeserializer extends ProjectDeserializer {

   @Override
   protected Project parse(InputStream input) throws Exception {
      var proto = ProjectProto.Project.parseFrom(input);

      return Project.builder()
              .name(proto.getName())
              .runner(proto.getRunner())
              .tileSetAssets(proto.getTileSetsList().stream().map(this::parseTileSetAsset).collect(toList()))
              .autoTileSetAssets(proto.getAutoTilesList().stream().map(this::parseAutoTileSetAsset).collect(toList()))
              .mapAssets(proto.getMapsList().stream().map(this::parseGameMapAsset).collect(toList()))
              .imageAssets(proto.getImagesList().stream().map(this::parseImageAsset).collect(toList()))
              .characterSetAssets(proto.getCharacterSetsList().stream().map(this::parseCharacterSetAsset).collect(toList()))
              .animationAssets(proto.getAnimationsList().stream().map(this::parseAnimationAsset).collect(toList()))
              .iconSetAssets(proto.getIconSetsList().stream().map(this::parseIconSetAsset).collect(toList()))
              .fontAssets(proto.getFontsList().stream().map(this::parseFontAsset).collect(toList()))
              .widgetDefinitionAssets(proto.getWidgetsList().stream().map(this::parseWidgetAsset).collect(toList()))
              .soundAssets(proto.getSoundsList().stream().map(this::parseSoundAsset).collect(toList()))
              .build();
   }

   private AutoTileSetAsset parseAutoTileSetAsset(ProjectProto.AutoTileSetAsset proto) {
      return new AutoTileSetAsset(proto.getUid(), proto.getSource(), proto.getRows(), proto.getColumns());
   }

   private TileSetAsset parseTileSetAsset(ProjectProto.TileSetAsset proto) {
      return new TileSetAsset(proto.getUid(), proto.getSource(), proto.getRows(), proto.getColumns());
   }

   private GameMapAsset parseGameMapAsset(ProjectProto.GameMapAsset proto) {
      return new GameMapAsset(proto.getUid(), proto.getSource());
   }

   private ImageAsset parseImageAsset(ProjectProto.ImageAsset proto) {
      return new ImageAsset(proto.getUid(), proto.getSource());
   }

   private CharacterSetAsset parseCharacterSetAsset(ProjectProto.CharacterSetAsset proto) {
      return new CharacterSetAsset(proto.getUid(), proto.getSource(), proto.getRows(), proto.getColumns());
   }

   private FontAsset parseFontAsset(ProjectProto.FontAsset proto) {
      return new FontAsset(proto.getUid(), proto.getSource());
   }

   private WidgetDefinitionAsset parseWidgetAsset(ProjectProto.WidgetAsset proto) {
      return new WidgetDefinitionAsset(proto.getUid(), proto.getSource());
   }

   private AnimationAsset parseAnimationAsset(ProjectProto.AnimationAsset proto) {
      return new AnimationAsset(proto.getUid(), proto.getSource(), proto.getRows(), proto.getColumns());
   }

   private IconSetAsset parseIconSetAsset(ProjectProto.IconSetAsset proto) {
      return new IconSetAsset(proto.getUid(), proto.getSource(), proto.getRows(), proto.getColumns());
   }

   private SoundAsset parseSoundAsset(ProjectProto.SoundAsset proto) {
      return new SoundAsset(proto.getUid(), proto.getSource());
   }
}
