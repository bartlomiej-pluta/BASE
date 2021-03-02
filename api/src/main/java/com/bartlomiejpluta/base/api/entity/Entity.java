package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.geo.Vector;

public interface Entity {
   Vector getCoordinates();

   void setCoordinates(Vector coordinates);

   Movement prepareMovement(Direction direction);

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void setSpeed(float speed);
}
