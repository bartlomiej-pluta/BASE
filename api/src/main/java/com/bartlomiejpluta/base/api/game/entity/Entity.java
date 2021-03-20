package com.bartlomiejpluta.base.api.game.entity;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import org.joml.Vector2ic;

public interface Entity extends Placeable, Movable, Renderable, Updatable {
   void setStepSize(float x, float y);

   Vector2ic getCoordinates();

   void setCoordinates(Vector2ic coordinates);

   void setCoordinates(int x, int y);

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void setSpeed(float speed);

   void setAnimationSpeed(float speed);

   int chebyshevDistance(Entity other);

   int manhattanDistance(Entity other);

   Direction getDirectionTowards(Entity target);

   void onAdd(ObjectLayer layer);

   void onRemove(ObjectLayer layer);

   boolean isBlocking();

   void setBlocking(boolean blocking);
}
