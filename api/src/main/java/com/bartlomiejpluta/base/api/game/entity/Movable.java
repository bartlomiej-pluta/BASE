package com.bartlomiejpluta.base.api.game.entity;

public interface Movable {
   Movement prepareMovement(Direction direction);

   Movement getMovement();

   boolean isMoving();
}
