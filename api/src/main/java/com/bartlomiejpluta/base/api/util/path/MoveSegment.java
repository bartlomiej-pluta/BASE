package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movable;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

import java.util.Objects;

public class MoveSegment<T extends Movable> implements PathSegment<T> {
   private final Direction direction;
   private final boolean ignore;

   public MoveSegment(Direction direction) {
      this(direction, false);
   }

   public MoveSegment(Direction direction, boolean ignore) {
      this.direction = Objects.requireNonNull(direction);
      this.ignore = ignore;
   }

   @Override
   public boolean perform(T movable, ObjectLayer layer, float dt) {
      var movement = movable.prepareMovement(direction);

      if (ignore || layer.isTileReachable(movement.getTo())) {
         layer.pushMovement(movement);
         return true;
      }

      return false;
   }
}
