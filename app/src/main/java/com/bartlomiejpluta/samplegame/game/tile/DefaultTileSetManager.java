package com.bartlomiejpluta.samplegame.game.tile;

import com.bartlomiejpluta.samplegame.core.gl.object.texture.TextureManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTileSetManager implements TileSetManager {
   private final TextureManager textureManager;

   @Override
   public TileSet createTileSet(String tileSetFileName) {
      return new TileSet(textureManager.loadTexture(tileSetFileName));
   }
}
