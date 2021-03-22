package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.object.Placeable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Entity extends Placeable, Movable, Renderable, Updatable {

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
