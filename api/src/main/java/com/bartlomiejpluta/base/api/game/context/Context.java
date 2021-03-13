package com.bartlomiejpluta.base.api.game.context;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.gui.base.GUI;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.input.Input;
import com.bartlomiejpluta.base.api.game.runner.GameRunner;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.gc.Disposable;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;

public interface Context extends Updatable, Renderable, Disposable {
   GameRunner getGameRunner();

   Screen getScreen();

   Camera getCamera();

   Input getInput();

   String getProjectName();

   void openMap(String mapUid);

   Entity createEntity(String entitySetUid);

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
