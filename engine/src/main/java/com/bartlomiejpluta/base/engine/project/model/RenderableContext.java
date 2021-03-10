package com.bartlomiejpluta.base.engine.project.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.gui.GUI;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.render.NanoVGGUI;
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

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RenderableContext implements Context, Updatable, Renderable, Cleanable {
   private final EntityManager entityManager;
   private final ImageManager imageManager;
   private final MapManager mapManager;
   private final FontManager fontManager;
   private final ClassLoader classLoader;

   @Getter
   private Screen screen;
   private DefaultGameMap map;
   private MapHandler mapHandler;

   private final List<GUI> guis = new LinkedList<>();

   @Getter
   private Camera camera;

   public void init(Screen screen, Camera camera) {
      this.screen = screen;
      this.camera = camera;
   }

   @SneakyThrows
   @Override
   public void openMap(String mapUid) {
      map = mapManager.loadObject(mapUid);
      mapHandler = mapManager.loadHandler(this, mapUid);

      mapHandler.onOpen(this, map);
   }

   @Override
   public Entity createEntity(String entitySetUid) {
      return entityManager.createEntity(entitySetUid);
   }

   @Override
   public Image getImage(String imageUid) {
      return imageManager.loadObject(imageUid);
   }

   @Override
   public GUI newGUI() {
      var gui = new NanoVGGUI(fontManager);
      guis.add(gui);
      gui.init(screen);
      return gui;
   }

   public void input(Screen screen) {
      if (mapHandler != null) {
         mapHandler.input(screen);
      }
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
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      if (map != null) {
         map.render(screen, camera, shaderManager);
      }

      for (var gui : guis) {
         gui.render(screen, camera, shaderManager);
      }

      if (mapHandler != null) {
         mapHandler.postRender(screen);
      }
   }

   @Override
   public void cleanUp() {
      guis.forEach(GUI::dispose);
   }
}
