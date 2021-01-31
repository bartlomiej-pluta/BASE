package com.bartlomiejpluta.base.core.world.tileset.manager;

import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTileSetManager implements TileSetManager {
   private final TextureManager textureManager;

   @Override
   public TileSet createTileSet(String tileSetFileName, int rows, int columns) {
      var texture = textureManager.loadTexture(tileSetFileName);
      return new TileSet(texture, rows, columns, texture.getWidth() / columns, texture.getHeight() / rows);
   }
}
