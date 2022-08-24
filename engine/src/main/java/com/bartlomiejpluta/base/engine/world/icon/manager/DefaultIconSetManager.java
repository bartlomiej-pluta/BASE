package com.bartlomiejpluta.base.engine.world.icon.manager;

import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import com.bartlomiejpluta.base.engine.world.icon.asset.IconSetAsset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultIconSetManager implements IconSetManager {
   private final TextureManager textureManager;
   private final ResourcesManager resourcesManager;
   private final Map<String, IconSetAsset> assets = new HashMap<>();
   private final Map<String, ByteBuffer> iconSets = new HashMap<>();

   private final ProjectConfiguration configuration;

   @Override
   public void registerAsset(IconSetAsset asset) {
      log.info("Registering [{}] icon set asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public IconSetAsset getAsset(String uid) {
      return assets.get(uid);
   }

   @Override
   public Material loadObject(String uid) {
      var asset = assets.get(uid);

      if (asset == null) {
         throw new AppException("The icon set asset with UID: [%s] does not exist", uid);
      }

      var source = configuration.projectFile("iconsets", asset.getSource());
      var texture = textureManager.loadTexture(source, asset.getRows(), asset.getColumns());

      return Material.textured(texture);
   }

   @Override
   public ByteBuffer loadObjectByteBuffer(String uid) {
      var buffer = iconSets.get(uid);

      if (buffer == null) {
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The icon set asset with UID: [%s] does not exist", uid);
         }

         var source = configuration.projectFile("iconsets", asset.getSource());
         buffer = resourcesManager.loadResourceAsByteBuffer(source);
         log.info("Loading icon set from assets to cache under the key: [{}]", uid);
         iconSets.put(uid, buffer);
      }

      return buffer.duplicate();
   }
}
