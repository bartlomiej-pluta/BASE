package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.move.Movement;

public interface EntityStepOutListener extends Entity {
   void onEntityStepOut(Movement movement, Entity entity);
}
