package com.bartlomiejpluta.base.core.gl.object.texture;

import com.bartlomiejpluta.base.core.util.res.ResourcesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTextureManager implements TextureManager {
   private final ResourcesManager resourcesManager;
   private final Map<String, Texture> loadedTextures = new HashMap<>();

   @Override
   public Texture loadTexture(String textureFileName) {
      var texture = loadedTextures.get(textureFileName);

      if (texture == null) {
         log.info("Loading [{}] texture to cache", textureFileName);
         var buffer = resourcesManager.loadResourceAsByteBuffer(textureFileName);
         texture = new Texture(textureFileName, buffer);
         loadedTextures.put(textureFileName, texture);
      }

      return texture;
   }

   @Override
   public void cleanUp() {
      log.info("Disposing textures");
      loadedTextures.forEach((name, texture) -> texture.dispose());
      log.info("{} textures has been disposed", loadedTextures.size());
   }
}
