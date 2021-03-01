package com.bartlomiejpluta.base.game.project.model;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.logic.Updatable;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.game.image.manager.ImageManager;
import com.bartlomiejpluta.base.game.map.manager.MapManager;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import com.bartlomiejpluta.base.game.tileset.manager.TileSetManager;
import com.bartlomiejpluta.base.game.world.entity.manager.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RenderableContext implements Context, Updatable, Renderable {
   private final TileSetManager tileSetManager;
   private final MeshManager meshManager;
   private final TextureManager textureManager;
   private final EntityManager entityManager;
   private final ImageManager imageManager;
   private final MapManager mapManager;

   private GameMap map;

   @Override
   public void openMap(String mapUid) {
      map = mapManager.loadMap(mapUid);
   }

   @Override
   public void update(float dt) {
      if (map != null) {
         map.update(dt);
      }
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      if (map != null) {
         map.render(window, camera, shaderManager);
      }
   }
}
