package com.bartlomiejpluta.base.game.world.tileset.manager;

import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.game.world.tileset.model.TileSet;
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
public class DefaultTileSetManager implements TileSetManager {
   private final TextureManager textureManager;
   private final MeshManager meshManager;
   private final Map<String, TileSet> tileSets = new HashMap<>();

   @Override
   public TileSet createTileSet(String tileSetFileName, int rows, int columns) {
      var key = format("%dx%d__%s", rows, columns, tileSetFileName);
      var tileset = tileSets.get(key);

      if (tileset == null) {
         var texture = textureManager.loadTexture(tileSetFileName, rows, columns);
         var size = texture.getSpriteSize();
         var mesh = meshManager.createQuad(size.x, size.y, 0, 0);
         tileset = new TileSet(texture, mesh);
         log.info("Loading [{}] tileset to cache under the key: [{}]", tileSetFileName, key);
         tileSets.put(key, tileset);
      }

      return tileset;
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
