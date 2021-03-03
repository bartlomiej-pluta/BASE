package com.bartlomiejpluta.base.engine.core.gl.object.texture;

import com.bartlomiejpluta.base.engine.util.res.ResourcesManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTextureManager implements TextureManager {
   private final ResourcesManager resourcesManager;
   private final Map<String, Texture> loadedTextures = new HashMap<>();

   @Override
   public Texture loadTexture(String textureFileName) {
      return loadTexture(textureFileName, 1, 1);
   }

   @Override
   public Texture loadTexture(String textureFileName, int rows, int columns) {
      var key = format("%dx%d__%s", rows, columns, textureFileName);
      var texture = loadedTextures.get(key);

      if (texture == null) {
         log.info("Loading [{}] texture to cache under the key: [{}]", textureFileName, key);
         var buffer = resourcesManager.loadResourceAsByteBuffer(textureFileName);
         texture = new Texture(textureFileName, buffer, rows, columns);
         loadedTextures.put(key, texture);
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
