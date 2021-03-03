package com.bartlomiejpluta.base.api.game.entity;

import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import org.joml.Vector2i;

public interface Entity extends Placeable, Renderable, Updatable {
   void setStepSize(float x, float y);

   Vector2i getCoordinates();

   void setCoordinates(Vector2i coordinates);

   void setCoordinates(int x, int y);

   Movement prepareMovement(Direction direction);

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void setSpeed(float speed);

   void setAnimationSpeed(float speed);
}
