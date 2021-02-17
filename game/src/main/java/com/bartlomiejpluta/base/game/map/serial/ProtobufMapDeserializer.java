package com.bartlomiejpluta.base.game.map.serial;

import com.bartlomiejpluta.base.core.error.AppException;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import com.bartlomiejpluta.base.game.tileset.manager.TileSetManager;
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
   private final TileSetManager tileSetManager;

   @Override
   protected GameMap parse(InputStream input) throws Exception {
      var proto = GameMapProto.GameMap.parseFrom(input);
      var tileSet = tileSetManager.loadTileSet(proto.getTileSetUID());
      var map = new GameMap(tileSet, proto.getRows(), proto.getColumns());

      proto.getLayersList().forEach(layer -> deserializeLayer(map, layer));

      return map;
   }

   private void deserializeLayer(GameMap map, GameMapProto.Layer proto) {
      if (proto.hasTileLayer()) {
         deserializeTileLayer(map, proto);
      } else if (proto.hasObjectLayer()) {
         deserializeObjectLayer(map, proto);
      } else if (proto.hasImageLayer()) {
         deserializeImageLayer(map, proto);
      } else {
         throw new AppException("Not supported layer type");
      }
   }

   private void deserializeTileLayer(GameMap map, GameMapProto.Layer proto) {
      var layer = map.createTileLayer();
      var columns = map.getColumns();
      var tiles = proto.getTileLayer().getTilesList();

      for (var i = 0; i < tiles.size(); ++i) {
         var tile = tiles.get(i);

         if (tile == 0) {
            map.clearTile(layer, i / columns, i % columns);
         } else {
            map.setTile(layer, i / columns, i % columns, tile - 1);
         }
      }
   }

   private void deserializeObjectLayer(GameMap map, GameMapProto.Layer proto) {
      var layer = map.createObjectLayer();
   }

   private void deserializeImageLayer(GameMap map, GameMapProto.Layer proto) {
      // TODO(return new ImageLayer(...))
   }
}
