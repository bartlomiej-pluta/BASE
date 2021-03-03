package com.bartlomiejpluta.base.engine.world.map.manager;

import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import com.bartlomiejpluta.base.engine.world.map.serial.MapDeserializer;
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
   private final Map<String, DefaultGameMap> maps = new HashMap<>();
   private final Map<String, GameMapAsset> assets = new HashMap<>();
   private final MapDeserializer mapDeserializer;
   private final ProjectConfiguration configuration;

   @Override
   public void registerAsset(GameMapAsset asset) {
      log.info("Registering [{}] map asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public DefaultGameMap loadObject(String uid) {
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
