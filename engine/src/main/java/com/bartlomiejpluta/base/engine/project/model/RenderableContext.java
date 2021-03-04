package com.bartlomiejpluta.base.engine.project.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.project.loader.ClassLoader;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntityManager;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.engine.world.map.manager.MapManager;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RenderableContext implements Context, Updatable, Renderable {
   private final EntityManager entityManager;
   private final ImageManager imageManager;
   private final MapManager mapManager;
   private final ClassLoader classLoader;

   @Getter
   private Window window;
   private DefaultGameMap map;
   private MapHandler mapHandler;

   @Getter
   private Camera camera;

   public void init(Window window, Camera camera) {
      this.window = window;
      this.camera = camera;
   }

   @SneakyThrows
   @Override
   public void openMap(String mapUid) {
      map = mapManager.loadObject(mapUid);

      var handlerClass = classLoader.<MapHandler>loadClass(map.getHandler());
      mapHandler = handlerClass.getConstructor().newInstance();

      mapHandler.init(this, map);
   }

   @Override
   public Entity createEntity(String entitySetUid) {
      return entityManager.createEntity(entitySetUid);
   }

   @Override
   public Image getImage(String imageUid) {
      return imageManager.loadObject(imageUid);
   }

   public void input(Window window) {
      mapHandler.input(window);
   }

   @Override
   public void update(float dt) {
      if (mapHandler != null) {
         mapHandler.update(this, map, dt);
      }

      if (map != null) {
         map.update(dt);
      }
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      if (map != null) {
         map.render(window, camera, shaderManager);
      }

      if (mapHandler != null) {
         mapHandler.postRender(window);
      }
   }
}
