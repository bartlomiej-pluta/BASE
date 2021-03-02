package com.bartlomiejpluta.base.game.project.loader;

import com.bartlomiejpluta.base.game.entity.manager.EntitySetManager;
import com.bartlomiejpluta.base.game.image.manager.ImageManager;
import com.bartlomiejpluta.base.game.map.manager.MapManager;
import com.bartlomiejpluta.base.game.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.game.project.model.Project;
import com.bartlomiejpluta.base.game.project.serial.ProjectDeserializer;
import com.bartlomiejpluta.base.game.tileset.manager.TileSetManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultProjectLoader implements ProjectLoader {
   private final ProjectConfiguration configuration;
   private final ProjectDeserializer projectDeserializer;
   private final TileSetManager tileSetManager;
   private final MapManager mapManager;
   private final ImageManager imageManager;
   private final EntitySetManager entitySetManager;

   @Override
   public Project loadProject() {
      var resource = DefaultProjectLoader.class.getResourceAsStream(configuration.projectFile(configuration.getMainFile()));
      var project = projectDeserializer.deserialize(resource);
      project.getTileSetAssets().forEach(tileSetManager::registerAsset);
      project.getMapAssets().forEach(mapManager::registerAsset);
      project.getImageAssets().forEach(imageManager::registerAsset);
      project.getEntitySetAssets().forEach(entitySetManager::registerAsset);

      return project;
   }
}
