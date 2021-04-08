package com.bartlomiejpluta.base.api.context;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.audio.Sound;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.image.Image;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.gc.Disposable;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.util.lambda.UncheckedConsumer;
import com.bartlomiejpluta.base.util.lambda.UncheckedFunction;

import java.sql.Connection;
import java.sql.SQLException;

public interface Context extends Updatable, Renderable, Disposable {
   GameRunner getGameRunner();

   Screen getScreen();

   Camera getCamera();

   Input getInput();

   String getProjectName();

   GameMap getMap();

   void openMap(String mapUid);

   void closeMap();

   Entity createEntity(String entitySetUid);

   Animation createAnimation(String animationUid);

   Image getImage(String imageUid);

   GUI newGUI();

   Sound createSound(String soundUid);

   void withDatabase(UncheckedConsumer<Connection, SQLException> consumer);

   <T> T withDatabase(UncheckedFunction<Connection, T, SQLException> extractor);

   void disposeSound(Sound sound);

   void playSound(String soundUid);

   void playSound(String soundUid, float gain);

   boolean isRunning();

   void close();

   boolean isPaused();

   void setPaused(boolean paused);

   void pause();

   void resume();

   boolean togglePause();

   void init(Screen screen, Input input, Camera camera);

   void input(Input input);
}
