package com.bartlomiejpluta.base.game.project.model;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Keyboard;
import com.bartlomiejpluta.base.api.map.MapHandler;
import com.bartlomiejpluta.base.core.gl.object.texture.TextureManager;
import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.logic.Updatable;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.util.mesh.MeshManager;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.game.image.manager.ImageManager;
import com.bartlomiejpluta.base.game.input.GLFWKeyboard;
import com.bartlomiejpluta.base.game.map.manager.MapManager;
import com.bartlomiejpluta.base.game.map.model.GameMap;
import com.bartlomiejpluta.base.game.project.loader.ClassLoader;
import com.bartlomiejpluta.base.game.tileset.manager.TileSetManager;
import com.bartlomiejpluta.base.game.world.entity.manager.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
   private final ClassLoader classLoader;

   private Keyboard keyboard;
   private GameMap map;
   private MapHandler mapHandler;

   public void init(Window window) {
      keyboard = new GLFWKeyboard(window);
   }

   @SneakyThrows
   @Override
   public void openMap(String mapUid) {
      map = mapManager.loadMap(mapUid);

      var handlerClass = classLoader.<MapHandler>loadClass(map.getHandler());
      mapHandler = handlerClass.getConstructor().newInstance();

      mapHandler.init(this);
   }

   public void input() {
      mapHandler.input(keyboard);
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
