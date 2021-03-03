package com.bartlomiejpluta.base.api.game.context;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.image.Image;

public interface Context {
   void openMap(String mapUid);

   Entity createEntity(String entitySetUid);

   Image getImage(String imageUid);

   Camera getCamera();
}
