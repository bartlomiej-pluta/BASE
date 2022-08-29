package com.bartlomiejpluta.base.engine.world.autotile.manager;

import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.engine.error.AppException;
import com.bartlomiejpluta.base.engine.project.config.ProjectConfiguration;
import com.bartlomiejpluta.base.engine.util.mesh.MeshManager;
import com.bartlomiejpluta.base.engine.world.autotile.asset.AutoTileSetAsset;
import com.bartlomiejpluta.base.engine.world.autotile.model.AutoTileSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultAutoTileSetManager implements AutoTileManager {
   private final TextureManager textureManager;
   private final MeshManager meshManager;
   private final Map<String, AutoTileSet> autoTiles = new HashMap<>();
   private final Map<String, AutoTileSetAsset> assets = new HashMap<>();
   private final ProjectConfiguration configuration;
   private Mesh mesh;

   @Override
   public void init() {
      this.mesh = meshManager.createQuad(1, 1, 0, 0);
   }

   @Override
   public void registerAsset(AutoTileSetAsset asset) {
      log.info("Registering [{}] auto tile set asset under UID: [{}]", asset.getSource(), asset.getUid());
      assets.put(asset.getUid(), asset);
   }

   @Override
   public AutoTileSetAsset getAsset(String uid) {
      return assets.get(uid);
   }

   @Override
   public AutoTileSet loadObject(String uid) {
      var autoTile = autoTiles.get(uid);

      if (autoTile == null) {
         var asset = assets.get(uid);

         if (asset == null) {
            throw new AppException("The auto tile set asset with UID: [%s] does not exist", uid);
         }

         var source = configuration.projectFile("autotiles", asset.getSource());
         var texture = textureManager.loadTexture(source, asset.getRows() * AutoTileSet.ROWS, asset.getColumns() * AutoTileSet.COLUMNS);
         autoTile = new AutoTileSet(texture, mesh, asset.getRows(), asset.getColumns());
         log.info("Loading auto tile set from assets to cache under the key: [{}]", uid);
         autoTiles.put(uid, autoTile);
      }

      return autoTile;
   }
}
