package com.bartlomiejpluta.base.api.context;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.audio.Sound;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.event.Event;
import com.bartlomiejpluta.base.api.event.EventType;
import com.bartlomiejpluta.base.api.gui.GUI;
import com.bartlomiejpluta.base.api.icon.Icon;
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
import java.util.function.Consumer;

public interface Context extends Updatable, Renderable, Disposable {
   GameRunner getGameRunner();

   Screen getScreen();

   Camera getCamera();

   Input getInput();

   String getProjectName();

   GameMap getMap();

   void openMap(String mapUid);

   void closeMap();

   Character createCharacter(String characterSetUid);

   Animation createAnimation(String animationUid);

   Entity createAbstractEntity();

   Icon createIcon(String iconSetUid, int row, int column);

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

   <E extends Event> void fireEvent(E event);

   <E extends Event> void addEventListener(EventType<E> type, Consumer<E> listener);

   <E extends Event> void removeEventListener(EventType<E> type, Consumer<E> listener);

   void putGlobal(String key, Object value);

   void putGlobal(String key, boolean value);

   void putGlobal(String key, byte value);

   void putGlobal(String key, short value);

   void putGlobal(String key, int value);

   void putGlobal(String key, long value);

   void putGlobal(String key, float value);

   void putGlobal(String key, double value);

   void putGlobal(String key, char value);

   Object getGlobal(String key);

   <T> T getGlobal(String key, Class<T> type);

   boolean getBooleanGlobal(String key);

   byte getByteGlobal(String key);

   short getShortGlobal(String key);

   int getIntGlobal(String key);

   long getLongGlobal(String key);

   float getFloatGlobal(String key);

   double getDoubleGlobal(String key);

   char getCharGlobal(String key);

   String getStringGlobal(String key);

   void init(Screen screen, Input input, Camera camera);

   void input(Input input);
}
