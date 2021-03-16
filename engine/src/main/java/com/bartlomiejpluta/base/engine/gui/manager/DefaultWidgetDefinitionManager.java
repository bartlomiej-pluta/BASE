package com.bartlomiejpluta.base.engine.gui.manager;

import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.asset.WidgetDefinitionAsset;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultWidgetDefinitionManager implements WidgetDefinitionManager {
   private final Map<String, WidgetDefinitionAsset> assets = new HashMap<>();
   private final ProjectConfiguration projectConfiguration;
   private final ResourcesManager resourcesManager;

   @Override
   public void registerAsset(WidgetDefinitionAsset asset) {
      log.info("Registering [{}] widget definition asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public InputStream loadObject(String uid) {
      var asset = assets.get(uid);

      if (asset == null) {
         throw new AppException("The widget definition asset with UID: [%s] does not exist", uid);
      }

      var source = projectConfiguration.projectFile("widgets", asset.getSource());

      return resourcesManager.loadResourceAsStream(source);
   }
}
