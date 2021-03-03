package com.bartlomiejpluta.base.api.game.context;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.entity.Entity;

public interface Context {
   void openMap(String mapUid);

   Entity createEntity(String entitySetUid);

   Camera getCamera();
}
