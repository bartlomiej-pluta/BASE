package com.bartlomiejpluta.base.engine.context.model;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.audio.Sound;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.context.GamePauseEvent;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.icon.Icon;
import com.bartlomiejpluta.base.api.image.Image;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.audio.manager.SoundManager;
import com.bartlomiejpluta.base.engine.core.engine.GameEngine;
import com.bartlomiejpluta.base.engine.database.service.DatabaseService;
import com.bartlomiejpluta.base.engine.gui.manager.FontManager;
import com.bartlomiejpluta.base.engine.gui.manager.WidgetDefinitionManager;
import com.bartlomiejpluta.base.engine.gui.render.NanoVGGUI;
import com.bartlomiejpluta.base.engine.gui.xml.inflater.Inflater;
import com.bartlomiejpluta.base.engine.world.animation.manager.AnimationManager;
import com.bartlomiejpluta.base.engine.world.character.manager.CharacterManager;
import com.bartlomiejpluta.base.engine.world.entity.AbstractEntity;
import com.bartlomiejpluta.base.engine.world.icon.manager.IconManager;
import com.bartlomiejpluta.base.engine.world.icon.manager.IconSetManager;
import com.bartlomiejpluta.base.engine.world.image.manager.ImageManager;
import com.bartlomiejpluta.base.engine.world.map.manager.MapManager;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import com.bartlomiejpluta.base.lib.event.EventHandler;
import com.bartlomiejpluta.base.util.lambda.UncheckedConsumer;
import com.bartlomiejpluta.base.util.lambda.UncheckedFunction;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Builder
public class DefaultContext implements Context {

   @NonNull
   private final GameEngine engine;

   @NonNull
   private final CharacterManager characterManager;

   @NonNull
   private final AnimationManager animationManager;

   @NonNull
   private final IconManager iconManager;

   @NonNull
   private final IconSetManager iconSetManager;

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

   @NonNull
   private final SoundManager soundManager;

   @NonNull
   private final DatabaseService databaseService;

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

   @Getter
   private DefaultGameMap map;
   private MapHandler mapHandler;

   @Getter
   private boolean paused;

   private final List<GUI> guis = new LinkedList<>();
   private final List<Sound> sounds = new LinkedList<>();

   private final EventHandler eventHandler = new EventHandler();

   @SneakyThrows
   @Override
   public void init(@NonNull Screen screen, @NonNull Input input, @NonNull Camera camera) {
      this.screen = screen;
      this.input = input;
      this.camera = camera;

      input.addKeyEventHandler(this::fireEvent);

      gameRunner.init(this);
   }

   @Override
   public <E extends Event> void fireEvent(E event) {
      eventHandler.handleEvent(event);

      if (map == null || event.isConsumed()) {
         return;
      }

      map.handleEvent(event);
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
   public Character createCharacter(@NonNull String characterSetUid) {
      return characterManager.createCharacter(characterSetUid);
   }

   @Override
   public Animation createAnimation(@NonNull String animationUid) {
      return animationManager.loadObject(animationUid);
   }

   @Override
   public Entity createAbstractEntity() {
      return new AbstractEntity();
   }

   @Override
   public Icon createIcon(@NonNull String iconSetUid, int row, int column) {
      return iconManager.createIcon(iconSetUid, row, column);
   }

   @Override
   public Image getImage(@NonNull String imageUid) {
      return imageManager.loadObject(imageUid);
   }

   @Override
   public GUI newGUI() {
      log.info("Creating new GUI");
      var gui = new NanoVGGUI(this, fontManager, imageManager, iconSetManager, inflater, widgetDefinitionManager);

      guis.add(gui);
      gui.init(screen);
      return gui;
   }

   @Override
   public Sound createSound(String soundUid) {
      return soundManager.loadObject(soundUid);
   }

   @Override
   public void withDatabase(UncheckedConsumer<Connection, SQLException> consumer) {
      try (var connection = databaseService.getConnection()) {
         consumer.accept(connection);
      } catch (SQLException e) {
         log.error("SQL Error", e);
      }
   }

   @Override
   public <T> T withDatabase(UncheckedFunction<Connection, T, SQLException> extractor) {
      try (var connection = databaseService.getConnection()) {
         return extractor.apply(connection);
      } catch (SQLException e) {
         log.error("SQL Error", e);
         return null;
      }
   }

   @Override
   public void disposeSound(Sound sound) {
      soundManager.disposeSound(sound);
   }

   @Override
   public void playSound(String soundUid) {
      var sound = soundManager.loadObject(soundUid);
      sounds.add(sound);
      sound.play();
   }

   @Override
   public void playSound(String soundUid, float gain) {
      var sound = soundManager.loadObject(soundUid);
      sound.setGain(gain);
      sounds.add(sound);
      sound.play();
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
   public void setPaused(boolean paused) {
      this.paused = paused;
      sounds.forEach(this.paused ? Sound::pause : Sound::play);
      fireEvent(new GamePauseEvent(this.paused));

   }

   @Override
   public void pause() {
      this.paused = true;
      sounds.forEach(Sound::pause);
      fireEvent(new GamePauseEvent(true));
   }

   @Override
   public void resume() {
      this.paused = false;
      sounds.forEach(Sound::play);
      fireEvent(new GamePauseEvent(false));
   }

   @Override
   public boolean togglePause() {
      this.paused = !this.paused;
      sounds.forEach(this.paused ? Sound::pause : Sound::play);
      fireEvent(new GamePauseEvent(this.paused));

      return this.paused;
   }

   @Override
   public void resetMaps() {
      log.info("Resetting maps");
      mapManager.resetMaps();
   }

   @Override
   public <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener) {
      eventHandler.addListener(type, listener);
   }

   @Override
   public <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener) {
      eventHandler.removeListener(type, listener);
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

      if (!paused) {
         if (mapHandler != null) {
            mapHandler.update(this, map, dt);
         }

         if (map != null) {
            map.update(dt);
         }
      }

      for (var gui : guis) {
         gui.update(dt);
      }

      for (var iterator = sounds.iterator(); iterator.hasNext(); ) {
         var sound = iterator.next();
         if (sound.isStopped()) {
            iterator.remove();
            soundManager.disposeSound(sound);
         }
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
