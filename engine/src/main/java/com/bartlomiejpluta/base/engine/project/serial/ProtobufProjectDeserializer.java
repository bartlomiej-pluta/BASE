package com.bartlomiejpluta.base.engine.project.serial;

import com.bartlomiejpluta.base.engine.gui.asset.FontAsset;
import com.bartlomiejpluta.base.engine.gui.asset.WidgetDefinitionAsset;
import com.bartlomiejpluta.base.engine.project.model.Project;
import com.bartlomiejpluta.base.engine.world.entity.asset.EntitySetAsset;
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
      var name = proto.getName();
      var runner = proto.getRunner();
      var tileSetAssets = proto.getTileSetsList().stream().map(this::parseTileSetAsset).collect(toList());
      var mapAssets = proto.getMapsList().stream().map(this::parseGameMapAsset).collect(toList());
      var imageAssets = proto.getImagesList().stream().map(this::parseImageAsset).collect(toList());
      var entitySetAssets = proto.getEntitySetsList().stream().map(this::parseEntitySetAsset).collect(toList());
      var fontAssets = proto.getFontsList().stream().map(this::parseFontAsset).collect(toList());
      var widgetAssets = proto.getWidgetsList().stream().map(this::parseWidgetAsset).collect(toList());

      return new Project(name, runner, tileSetAssets, mapAssets, imageAssets, entitySetAssets, fontAssets, widgetAssets);
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

   private EntitySetAsset parseEntitySetAsset(ProjectProto.EntitySetAsset proto) {
      return new EntitySetAsset(proto.getUid(), proto.getSource(), proto.getRows(), proto.getColumns());
   }

   private FontAsset parseFontAsset(ProjectProto.FontAsset proto) {
      return new FontAsset(proto.getUid(), proto.getSource());
   }

   private WidgetDefinitionAsset parseWidgetAsset(ProjectProto.WidgetAsset proto) {
      return new WidgetDefinitionAsset(proto.getUid(), proto.getSource());
   }
}
