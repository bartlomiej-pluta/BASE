package com.bartlomiejpluta.base.api.entity;

import org.joml.Vector2f;
import org.joml.Vector2i;

public interface Entity {
   Vector2i getCoordinates();

   void setCoordinates(Vector2i coordinates);

   void setCoordinates(int x, int y);

   Vector2f getPosition();

   void setPosition(Vector2f position);

   void setPosition(float x, float y);

   Movement prepareMovement(Direction direction);

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void setSpeed(float speed);

   void setAnimationSpeed(float speed);
}
