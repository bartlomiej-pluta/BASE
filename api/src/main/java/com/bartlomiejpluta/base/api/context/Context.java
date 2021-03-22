package com.bartlomiejpluta.base.api.context;

import com.bartlomiejpluta.base.api.animation.Animation;
import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.gui.base.GUI;
import com.bartlomiejpluta.base.api.image.Image;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.runner.GameRunner;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.internal.gc.Disposable;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Context extends Updatable, Renderable, Disposable {
   GameRunner getGameRunner();

   Screen getScreen();

   Camera getCamera();

   Input getInput();

   String getProjectName();

   void openMap(String mapUid);

   void closeMap();

   Entity createEntity(String entitySetUid);

   Animation createAnimation(String animationUid);

   Image getImage(String imageUid);

   GUI newGUI();

   boolean isRunning();

   void close();

   boolean isPaused();

   void pause();

   void resume();

   boolean togglePause();

   void init(Screen screen, Input input, Camera camera);

   void input(Input input);
}
