package com.bartlomiejpluta.samplegame.game.world.tileset.manager;

import com.bartlomiejpluta.samplegame.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.samplegame.game.world.tileset.model.TileSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultTileSetManager implements TileSetManager {
   private final TextureManager textureManager;
   private final int tileWidth;
   private final int tileHeight;

   @Autowired
   public DefaultTileSetManager(
           TextureManager textureManager,
           @Value("${app.map.tile.width}") int tileWidth,
           @Value("${app.map.tile.width}") int tileHeight) {
      this.textureManager = textureManager;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
   }

   @Override
   public TileSet createTileSet(String tileSetFileName, int rows, int columns) {
      return new TileSet(textureManager.loadTexture(tileSetFileName), rows, columns, tileWidth, tileHeight);
   }
}
