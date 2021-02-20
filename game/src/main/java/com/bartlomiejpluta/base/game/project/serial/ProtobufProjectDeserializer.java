package com.bartlomiejpluta.base.game.project.serial;

import com.bartlomiejpluta.base.game.image.asset.ImageAsset;
import com.bartlomiejpluta.base.game.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.game.project.model.Project;
import com.bartlomiejpluta.base.game.tileset.asset.TileSetAsset;
import com.bartlomiejpluta.base.proto.ProjectProto;
import org.springframework.stereotype.Component;

import java.io.InputStream;

import static java.util.stream.Collectors.toList;

@Component
public class ProtobufProjectDeserializer extends ProjectDeserializer {

   @Override
   protected Project parse(InputStream input) throws Exception {
      var proto = ProjectProto.Project.parseFrom(input);
      var name = proto.getName();
      var tileSetAssets = proto.getTileSetsList().stream().map(this::parseTileSetAsset).collect(toList());
      var mapAssets = proto.getMapsList().stream().map(this::parseGameMapAsset).collect(toList());
      var imageAssets = proto.getImagesList().stream().map(this::parseImageAsset).collect(toList());
      return new Project(name, tileSetAssets, mapAssets, imageAssets);
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
}
