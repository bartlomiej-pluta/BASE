package com.bartlomiejpluta.base.engine.world.map.manager;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.reflection.ClassLoader;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import com.bartlomiejpluta.base.engine.world.map.serial.MapDeserializer;
import com.bartlomiejpluta.base.internal.map.MapInitializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultMapManager implements MapManager {
   private final Map<String, DefaultGameMap> maps = new HashMap<>();
   private final Map<String, GameMapAsset> assets = new HashMap<>();
   private final Map<String, MapHandler> handlers = new HashMap<>();
   private final MapDeserializer mapDeserializer;
   private final ProjectConfiguration configuration;
   private final ClassLoader classLoader;

   @Override
   public void registerAsset(GameMapAsset asset) {
      log.info("Registering [{}] map asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public GameMapAsset getAsset(String uid) {
      return assets.get(uid);
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

   @SneakyThrows
   @Override
   public MapHandler loadHandler(Context context, String uid) {
      var handler = handlers.get(uid);

      if (handler == null) {
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The map asset with UID: [%s] does not exist", uid);
         }

         var map = maps.get(uid);

         if (map == null) {
            throw new AppException("The map asset with UID: [%s] has not been loaded yet", uid);
         }

         var handlerClassName = map.getHandler();
         log.info("Creating new handler: [{}] for the map with ID: [{}]", handlerClassName, uid);
         var handlerClass = classLoader.<MapHandler>loadClass(handlerClassName);
         handler = handlerClass.getConstructor().newInstance();
         handlers.put(uid, handler);

         handler.onCreate(context, map);

         var layers = map.getLayers();
         for (var layer : layers) {
            if (layer instanceof ObjectLayer) {
               var packageName = "com.bartlomiejpluta.base.generated.map";
               var purifiedUid = uid.replace("-", "_");
               var layerIndex = layers.indexOf(layer);
               var className = format("%s.MapInitializer_%s$$Layer%d", packageName, purifiedUid, layerIndex);
               var initializerClass = classLoader.<MapInitializer>loadClass(className);
               var initializer = initializerClass.getConstructor().newInstance();
               initializer.run(context, handler, map, (ObjectLayer) layer);
            }
         }
      }

      return handler;
   }

   @Override
   public void resetMaps() {
      log.info("Removing maps and map handlers from cache");

      var mapsSize = maps.size();
      var handlersSize = handlers.size();
      maps.clear();
      handlers.clear();

      log.info("Removed {} maps and {} map handlers from cache", mapsSize, handlersSize);
   }
}
