package com.bartlomiejpluta.base.engine.world.map.serial;

import com.bartlomiejpluta.base.api.map.layer.image.ImageLayerMode;
import com.bartlomiejpluta.base.api.map.layer.object.PassageAbility;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import com.bartlomiejpluta.base.engine.world.tileset.manager.TileSetManager;
import com.bartlomiejpluta.base.proto.GameMapProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProtobufMapDeserializer extends MapDeserializer {
   private final MeshManager meshManager;
   private final TileSetManager tileSetManager;
   private final ImageManager imageManager;

   @Override
   protected DefaultGameMap parse(InputStream input) throws Exception {
      var proto = GameMapProto.GameMap.parseFrom(input);
      var map = new DefaultGameMap(proto.getTileWidth(), proto.getTileHeight(), proto.getRows(), proto.getColumns(), proto.getHandler());

      proto.getLayersList().forEach(layer -> deserializeLayer(map, layer));

      return map;
   }

   private void deserializeLayer(DefaultGameMap map, GameMapProto.Layer proto) {
      if (proto.hasTileLayer()) {
         deserializeTileLayer(map, proto);
      } else if (proto.hasObjectLayer()) {
         deserializeObjectLayer(map, proto);
      } else if (proto.hasColorLayer()) {
         deserializeColorLayer(map, proto);
      } else if (proto.hasImageLayer()) {
         deserializeImageLayer(map, proto);
      } else {
         throw new AppException("Not supported layer type");
      }
   }

   private void deserializeTileLayer(DefaultGameMap map, GameMapProto.Layer proto) {
      var layer = map.createTileLayer(tileSetManager.loadObject(proto.getTileLayer().getTilesetUID()));
      var columns = map.getColumns();
      var tiles = proto.getTileLayer().getTilesList();

      for (var i = 0; i < tiles.size(); ++i) {
         var tile = tiles.get(i);

         if (tile == 0) {
            layer.clearTile(i / columns, i % columns);
         } else {
            layer.setTile(i / columns, i % columns, tile - 1);
         }
      }
   }

   private void deserializeObjectLayer(DefaultGameMap map, GameMapProto.Layer proto) {
      var layer = map.createObjectLayer();
      var columns = map.getColumns();
      var passageMap = proto.getObjectLayer().getPassageMapList();

      for (var i = 0; i < passageMap.size(); ++i) {
         layer.setPassageAbility(i / columns, i % columns, switch (passageMap.get(i)) {
            case ALLOW -> PassageAbility.ALLOW;
            case BLOCK -> PassageAbility.BLOCK;
         });
      }
   }

   private void deserializeColorLayer(DefaultGameMap map, GameMapProto.Layer proto) {
      var protoColorLayer = proto.getColorLayer();
      map.createColorLayer(
              meshManager,
              protoColorLayer.getRed() / 100.0f,
              protoColorLayer.getGreen() / 100.0f,
              protoColorLayer.getBlue() / 100.0f,
              protoColorLayer.getAlpha() / 100.0f
      );
   }

   private void deserializeImageLayer(DefaultGameMap map, GameMapProto.Layer proto) {
      var protoImageLayer = proto.getImageLayer();
      var image = imageManager.loadObject(protoImageLayer.getImageUID());

      var mode = switch (proto.getImageLayer().getMode()) {
         case NORMAL -> ImageLayerMode.NORMAL;
         case FIT_MAP -> ImageLayerMode.FIT_MAP;
         case FIT_SCREEN -> ImageLayerMode.FIT_SCREEN;
      };

      map.createImageLayer(
              image,
              protoImageLayer.getOpacity() / 100.0f,
              protoImageLayer.getX(),
              protoImageLayer.getY(),
              (float) protoImageLayer.getScaleX(),
              (float) protoImageLayer.getScaleY(),
              mode,
              protoImageLayer.getParallax()
      );
   }
}
