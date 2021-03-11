package com.bartlomiejpluta.base.engine.gui.manager;

import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.gui.asset.FontAsset;
import com.bartlomiejpluta.base.engine.gui.model.Font;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultFontManager implements FontManager {
   private final Map<String, FontAsset> assets = new HashMap<>();
   private final Map<String, ByteBuffer> fontBuffers = new HashMap<>();
   private final ProjectConfiguration configuration;
   private final ResourcesManager resourcesManager;

   @Override
   public void registerAsset(FontAsset asset) {
      log.info("Registering [{}] font asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);

   }

   @Override
   public Font loadObject(String uid) {
      var buffer = fontBuffers.get(uid);

      if (buffer == null) {
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The font asset with UID: [%s] does not exist", uid);
         }

         var source = configuration.projectFile("fonts", asset.getSource());
         buffer = resourcesManager.loadResourceAsByteBuffer(source);
         log.info("Loading font from assets to cache under the key: [{}]", uid);
         fontBuffers.put(uid, buffer);
      }

      return new Font(uid, buffer.duplicate());
   }
}
