package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;
import com.bartlomiejpluta.base.internal.logic.Updatable;
import com.bartlomiejpluta.base.internal.render.Renderable;

public interface Entity extends Movable, Renderable, Updatable {

   Direction getFaceDirection();

   void setFaceDirection(Direction direction);

   void setSpeed(float speed);

   void setAnimationSpeed(float speed);

   int chebyshevDistance(Entity other);

   int manhattanDistance(Entity other);

   Direction getDirectionTowards(Entity target);

   ObjectLayer getLayer();

   void onAdd(ObjectLayer layer);

   void onRemove(ObjectLayer layer);

   boolean isBlocking();

   void setBlocking(boolean blocking);

   void changeEntitySet(String entitySetUid);

   int getZIndex();

   void setZIndex(int zIndex);
}
