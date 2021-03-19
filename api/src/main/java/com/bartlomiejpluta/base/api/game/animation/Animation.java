package com.bartlomiejpluta.base.api.game.animation;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.internal.logic.Updatable;
import com.bartlomiejpluta.base.api.internal.object.Placeable;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import org.joml.Vector2ic;

public interface Animation extends Placeable, Renderable, Updatable {
   void setStepSize(float x, float y);

   Vector2ic getCoordinates();

   void setCoordinates(Vector2ic coordinates);

   void setCoordinates(int x, int y);

   Movement prepareMovement(Direction direction);

   Movement getMovement();

   void setSpeed(float speed);

   void setAnimationSpeed(float speed);

   boolean isMoving();

   void onAdd(ObjectLayer layer);

   void onRemove(ObjectLayer layer);
}
