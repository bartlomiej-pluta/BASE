package com.bartlomiejpluta.base.api.map;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.entity.Movement;

public interface GameMap {
   void addEntity(int layerIndex, Entity entity);

   void removeEntity(int layerIndex, Entity entity);

   boolean isMovementPossible(int layerIndex, Movement movement);
}
