package com.bartlomiejpluta.base.api.move;

import org.joml.Vector2fc;
import org.joml.Vector2ic;

public interface Movable {
   void setStepSize(float x, float y);

   Vector2ic getCoordinates();

   void setCoordinates(Vector2ic coordinates);

   void setCoordinates(int x, int y);

   void setPositionOffset(Vector2fc offset);

   void setPositionOffset(float offsetX, float offsetY);

   void setSpeed(float speed);

   Movement prepareMovement(Direction direction);

   Movement getMovement();

   boolean isMoving();

   int chebyshevDistance(Vector2ic coordinates);

   int manhattanDistance(Vector2ic coordinates);
}
