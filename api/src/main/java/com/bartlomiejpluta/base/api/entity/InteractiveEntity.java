package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.move.Movement;

public interface InteractiveEntity extends Entity {
   void onEntityStepIn(Movement movement, Entity entity);

   void onEntityStepOut(Movement movement, Entity entity);
}
