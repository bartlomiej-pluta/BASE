package com.bartlomiejpluta.base.game.tileset.manager;

import com.bartlomiejpluta.base.core.error.AppException;
import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.game.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.game.tileset.asset.TileSetAsset;
import com.bartlomiejpluta.base.game.tileset.model.TileSet;
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
   private final Map<String, TileSetAsset> assets = new HashMap<>();
   private final ProjectConfiguration configuration;

   @Override
   public void registerAsset(TileSetAsset asset) {
      log.info("Registering [{}] tile set asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public TileSet loadObject(String uid) {
      var tileset = tileSets.get(uid);

      if (tileset == null) {
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The tile set asset with UID: [%s] does not exist", uid);
         }

         var source = configuration.projectFile("tilesets", asset.getSource());
         var texture = textureManager.loadTexture(source, asset.getRows(), asset.getColumns());
         var size = texture.getSpriteSize();
         var mesh = meshManager.createQuad(size.x, size.y, 0, 0);
         tileset = new TileSet(texture, mesh);
         log.info("Loading tileset from assets to cache under the key: [{}]", uid);
         tileSets.put(uid, tileset);
      }

      return tileset;
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
