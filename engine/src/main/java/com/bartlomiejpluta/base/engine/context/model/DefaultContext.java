package com.bartlomiejpluta.base.engine.context.model;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.game.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.core.engine.GameEngine;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.manager.WidgetDefinitionManager;
import com.bartlomiejpluta.base.engine.gui.render.NanoVGGUI;
import com.bartlomiejpluta.base.engine.gui.xml.inflater.Inflater;
import com.bartlomiejpluta.base.engine.world.entity.manager.EntityManager;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.engine.world.map.manager.MapManager;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Builder
public class DefaultContext implements Context {

   @NonNull
   private final GameEngine engine;

   @NonNull
   private final EntityManager entityManager;

   @NonNull
   private final ImageManager imageManager;

   @NonNull
   private final MapManager mapManager;

   @NonNull
   private final FontManager fontManager;

   @NonNull
   private final Inflater inflater;

   @NonNull
   private final WidgetDefinitionManager widgetDefinitionManager;

   @Getter
   @NonNull
   private final GameRunner gameRunner;

   @Getter
   @NonNull
   private final String projectName;

   @Getter
   private Input input;

   @Getter
   private Screen screen;

   @Getter
   private Camera camera;

   private DefaultGameMap map;
   private MapHandler mapHandler;

   private final List<GUI> guis = new LinkedList<>();

   @SneakyThrows
   @Override
   public void init(@NonNull Screen screen, @NonNull Input input, @NonNull Camera camera) {
      this.screen = screen;
      this.input = input;
      this.camera = camera;

      gameRunner.init(this);
   }

   @SneakyThrows
   @Override
   public void openMap(@NonNull String mapUid) {
      log.info("Opening map with UID: [{}]", mapUid);
      map = mapManager.loadObject(mapUid);
      mapHandler = mapManager.loadHandler(this, mapUid);

      mapHandler.onOpen(this, map);
   }

   @Override
   public void closeMap() {
      log.info("Closing map");
      map = null;
      mapHandler = null;
   }

   @Override
   public Entity createEntity(@NonNull String entitySetUid) {
      log.info("Creating new entity with UID: [{}]", entitySetUid);
      return entityManager.createEntity(entitySetUid);
   }

   @Override
   public Image getImage(@NonNull String imageUid) {
      return imageManager.loadObject(imageUid);
   }

   @Override
   public GUI newGUI() {
      log.info("Creating new GUI");
      var gui = new NanoVGGUI(this, fontManager, imageManager, inflater, widgetDefinitionManager);

      guis.add(gui);
      gui.init(screen);
      return gui;
   }

   @Override
   public boolean isRunning() {
      return engine.isRunning();
   }

   @Override
   public void close() {
      engine.stop();
   }

   @Override
   public boolean isPaused() {
      return engine.isPaused();
   }

   @Override
   public void pause() {
      engine.pause();
   }

   @Override
   public void resume() {
      engine.resume();
   }

   @Override
   public boolean togglePause() {
      return engine.togglePaused();
   }

   @Override
   public void input(Input input) {
      gameRunner.input(input);

      if (mapHandler != null) {
         mapHandler.input(input);
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
   public void dispose() {
      log.info("Disposing GUIs");
      guis.forEach(GUI::dispose);

      log.info("Disposing game runner");
      gameRunner.dispose();
   }
}
