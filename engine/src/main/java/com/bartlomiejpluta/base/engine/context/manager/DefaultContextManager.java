package com.bartlomiejpluta.base.engine.context.manager;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;
import com.bartlomiejpluta.base.engine.context.model.DefaultContext;
import com.bartlomiejpluta.base.engine.core.engine.GameEngine;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.manager.WidgetDefinitionManager;
import com.bartlomiejpluta.base.engine.gui.xml.inflater.Inflater;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.project.serial.ProjectDeserializer;
import com.bartlomiejpluta.base.engine.util.reflection.ClassLoader;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntityManager;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntitySetManager;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.engine.world.map.manager.MapManager;
import com.bartlomiejpluta.base.engine.world.tileset.manager.TileSetManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultContextManager implements ContextManager {
   private final GameEngine engine;
   private final ProjectConfiguration configuration;
   private final ProjectDeserializer projectDeserializer;
   private final TileSetManager tileSetManager;
   private final MapManager mapManager;
   private final ImageManager imageManager;
   private final EntitySetManager entitySetManager;
   private final FontManager fontManager;
   private final EntityManager entityManager;
   private final ClassLoader classLoader;
   private final Inflater inflater;
   private final WidgetDefinitionManager widgetDefinitionManager;

   @SneakyThrows
   @Override
   public Context createContext() {
      log.info("Deserializing project file");
      var resource = DefaultContextManager.class.getResourceAsStream(configuration.projectFile(configuration.getMainFile()));
      var project = projectDeserializer.deserialize(resource);

      log.info("Registering project assets");
      project.getTileSetAssets().forEach(tileSetManager::registerAsset);
      project.getMapAssets().forEach(mapManager::registerAsset);
      project.getImageAssets().forEach(imageManager::registerAsset);
      project.getEntitySetAssets().forEach(entitySetManager::registerAsset);
      project.getFontAssets().forEach(fontManager::registerAsset);
      project.getWidgetDefinitionAssets().forEach(widgetDefinitionManager::registerAsset);

      log.info("Creating game runner instance");
      var runnerClass = classLoader.<GameRunner>loadClass(project.getRunner());
      var gameRunner = runnerClass.getConstructor().newInstance();

      log.info("Building context up");
      return DefaultContext.builder()
            .engine(engine)
            .entityManager(entityManager)
            .imageManager(imageManager)
            .mapManager(mapManager)
            .fontManager(fontManager)
            .inflater(inflater)
            .widgetDefinitionManager(widgetDefinitionManager)
            .gameRunner(gameRunner)
            .projectName(project.getName())
            .build();
   }
}
