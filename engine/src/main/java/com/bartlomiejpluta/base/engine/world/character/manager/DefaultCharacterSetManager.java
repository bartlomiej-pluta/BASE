package com.bartlomiejpluta.base.engine.world.character.manager;

import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.world.character.asset.CharacterSetAsset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultCharacterSetManager implements CharacterSetManager {
   private final TextureManager textureManager;
   private final Map<String, CharacterSetAsset> assets = new HashMap<>();
   private final ProjectConfiguration configuration;

   @Override
   public void registerAsset(CharacterSetAsset asset) {
      log.info("Registering [{}] character set asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public Material loadObject(String uid) {
      var asset = assets.get(uid);

      if (asset == null) {
         throw new AppException("The character set asset with UID: [%s] does not exist", uid);
      }

      var source = configuration.projectFile("charsets", asset.getSource());
      var texture = textureManager.loadTexture(source, asset.getRows(), asset.getColumns());

      return Material.textured(texture);
   }
}
