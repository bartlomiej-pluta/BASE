package com.bartlomiejpluta.base.game.map.manager;

import com.bartlomiejpluta.base.core.error.AppException;
import com.bartlomiejpluta.base.game.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import com.bartlomiejpluta.base.game.map.serial.MapDeserializer;
import com.bartlomiejpluta.base.game.project.config.ProjectConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultMapManager implements MapManager {
   private final Map<String, GameMap> maps = new HashMap<>();
   private final Map<String, GameMapAsset> assets = new HashMap<>();
   private final MapDeserializer mapDeserializer;
   private final ProjectConfiguration configuration;

   @Override
   public void registerAsset(GameMapAsset asset) {
      log.info("Registering [{}] map asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public GameMap loadObject(String uid) {
      var map = maps.get(uid);

      if (map == null) {
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The map asset with UID: [%s] does not exist", uid);
         }

         var source = configuration.projectFile("maps", asset.getSource());
         map = mapDeserializer.deserialize(DefaultMapManager.class.getResourceAsStream(source));
         log.info("Loading map from assets to cache under the key: [{}]", uid);
         maps.put(uid, map);
      }

      return map;
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
