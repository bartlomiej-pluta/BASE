package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.move.Movement;

public interface EntityStepInListener extends Entity {
   void onEntityStepIn(Movement movement, Entity entity);
}
