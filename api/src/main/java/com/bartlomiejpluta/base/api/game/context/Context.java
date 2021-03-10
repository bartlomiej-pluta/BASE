package com.bartlomiejpluta.base.api.game.context;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.gui.GUI;
import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.api.game.screen.Screen;

public interface Context {
   Screen getScreen();

   Camera getCamera();

   void openMap(String mapUid);

   Entity createEntity(String entitySetUid);

   Image getImage(String imageUid);

   GUI newGUI();
}
