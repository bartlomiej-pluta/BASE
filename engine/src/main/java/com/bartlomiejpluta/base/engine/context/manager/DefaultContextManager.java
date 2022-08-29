package com.bartlomiejpluta.base.engine.context.manager;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.ContextHolder;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.engine.audio.manager.SoundManager;
import com.bartlomiejpluta.base.engine.context.model.DefaultContext;
import com.bartlomiejpluta.base.engine.core.engine.GameEngine;
import com.bartlomiejpluta.base.engine.database.service.DatabaseService;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.manager.WidgetDefinitionManager;
import com.bartlomiejpluta.base.engine.gui.xml.inflater.Inflater;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.project.serial.ProjectDeserializer;
import com.bartlomiejpluta.base.engine.util.reflection.ClassLoader;
import com.bartlomiejpluta.base.engine.world.animation.manager.AnimationManager;
import com.bartlomiejpluta.base.engine.world.autotile.manager.AutoTileManager;
import com.bartlomiejpluta.base.engine.world.character.manager.CharacterManager;
import com.bartlomiejpluta.base.engine.world.character.manager.CharacterSetManager;
import com.bartlomiejpluta.base.engine.world.icon.manager.IconManager;
import com.bartlomiejpluta.base.engine.world.icon.manager.IconSetManager;
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
   private final AutoTileManager autoTileManager;
   private final MapManager mapManager;
   private final ImageManager imageManager;
   private final CharacterSetManager characterSetManager;
   private final FontManager fontManager;
   private final IconSetManager iconSetManager;
   private final CharacterManager characterManager;
   private final AnimationManager animationManager;
   private final IconManager iconManager;
   private final ClassLoader classLoader;
   private final Inflater inflater;
   private final WidgetDefinitionManager widgetDefinitionManager;
   private final SoundManager soundManager;
   private final DatabaseService databaseService;

   @SneakyThrows
   @Override
   public Context createContext() {
      log.info("Deserializing project file");
      var resource = DefaultContextManager.class.getResourceAsStream(configuration.projectFile(configuration.getMainFile()));
      var project = projectDeserializer.deserialize(resource);

      log.info("Registering project assets");
      project.getTileSetAssets().forEach(tileSetManager::registerAsset);
      project.getAutoTileSetAssets().forEach(autoTileManager::registerAsset);
      project.getMapAssets().forEach(mapManager::registerAsset);
      project.getImageAssets().forEach(imageManager::registerAsset);
      project.getCharacterSetAssets().forEach(characterSetManager::registerAsset);
      project.getAnimationAssets().forEach(animationManager::registerAsset);
      project.getIconSetAssets().forEach(iconSetManager::registerAsset);
      project.getFontAssets().forEach(fontManager::registerAsset);
      project.getWidgetDefinitionAssets().forEach(widgetDefinitionManager::registerAsset);
      project.getSoundAssets().forEach(soundManager::registerAsset);

      log.info("Creating game runner instance");
      var runnerClass = classLoader.<GameRunner>loadClass(project.getRunner());
      var gameRunner = runnerClass.getConstructor().newInstance();

      log.info("Building context up");
      var context = DefaultContext.builder()
              .engine(engine)
              .characterManager(characterManager)
              .animationManager(animationManager)
              .iconManager(iconManager)
              .iconSetManager(iconSetManager)
              .imageManager(imageManager)
              .mapManager(mapManager)
              .fontManager(fontManager)
              .inflater(inflater)
              .widgetDefinitionManager(widgetDefinitionManager)
              .soundManager(soundManager)
              .databaseService(databaseService)
              .gameRunner(gameRunner)
              .projectName(project.getName())
              .build();

      ContextHolder.INSTANCE.setContext(context);

      return context;
   }
}
