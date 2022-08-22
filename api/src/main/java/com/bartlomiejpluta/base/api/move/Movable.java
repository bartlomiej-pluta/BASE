package com.bartlomiejpluta.base.api.move;

import com.bartlomiejpluta.base.api.location.Locationable;

public interface Movable extends Locationable {
   void setSpeed(float speed);

   Movement prepareMovement(Direction direction);

   Movement getMovement();

   boolean isMoving();

   boolean move(Movement movement);

   void abortMove();
}
