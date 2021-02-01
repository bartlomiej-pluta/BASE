package com.bartlomiejpluta.base.core.world.tileset.manager;

import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTileSetManager implements TileSetManager {
   private final TextureManager textureManager;
   private final MeshManager meshManager;
   private final Map<String, TileSet> tileSets = new HashMap<>();

   @Override
   public TileSet createTileSet(String tileSetFileName, int rows, int columns) {
      var tileset = tileSets.get(tileSetFileName);

      if(tileset == null) {
         log.info("Loading [{}] tileset to cache", tileSetFileName);
         var texture = textureManager.loadTexture(tileSetFileName);
         var tileWidth = texture.getWidth() / columns;
         var tileHeight = texture.getHeight() / rows;
         var mesh = meshManager.createQuad(tileWidth, tileHeight, 0, 0);
         tileset =  new TileSet(mesh, texture, rows, columns, tileWidth, tileHeight);
      }

      return tileset;
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
