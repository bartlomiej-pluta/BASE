package com.bartlomiejpluta.base.engine.project.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Slf4j
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

   @Getter
   private GameRunner gameRunner;

   @Getter
   private Camera camera;

   private Project project;
   private DefaultGameMap map;
   private MapHandler mapHandler;

   private final List<GUI> guis = new LinkedList<>();

   @SneakyThrows
   public void init(Screen screen, Camera camera, Project project) {
      log.info("Initializing game context");
      this.screen = screen;
      this.camera = camera;
      this.project = project;

      var runnerClass = classLoader.<GameRunner>loadClass(project.getRunner());
      gameRunner = runnerClass.getConstructor().newInstance();

      gameRunner.init(this);
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
      log.info("Creating new entity with UID: [{}]", entitySetUid);
      return entityManager.createEntity(entitySetUid);
   }

   @Override
   public Image getImage(String imageUid) {
      return imageManager.loadObject(imageUid);
   }

   @Override
   public GUI newGUI() {
      log.info("Creating new GUI");
      var gui = new NanoVGGUI(fontManager);
      guis.add(gui);
      gui.init(screen);
      return gui;
   }

   public void input(Screen screen) {
      gameRunner.input(screen);

      if (mapHandler != null) {
         mapHandler.input(screen);
      }
   }

   @Override
   public void update(float dt) {
      gameRunner.update(dt);

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
      log.info("Disposing game runner");
      gameRunner.dispose();

      log.info("Disposing GUIs");
      guis.forEach(GUI::dispose);
   }
}
