package com.bartlomiejpluta.base.api.game.context;

import com.bartlomiejpluta.base.api.game.entity.Entity;
import org.joml.Vector2f;

public interface Context {
   void openMap(String mapUid);

   Entity createEntity(String entitySetUid);

   void setCameraPosition(Vector2f position);

   void setCameraPosition(float x, float y);
}
