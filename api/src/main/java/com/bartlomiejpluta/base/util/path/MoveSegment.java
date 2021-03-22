package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;

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
   public PathProgress perform(T movable, ObjectLayer layer, float dt) {
      if (movable.isMoving()) {
         return PathProgress.ONGOING;
      }

      var movement = movable.prepareMovement(direction);

      if (ignore || layer.isTileReachable(movement.getTo())) {
         layer.pushMovement(movement);
         return PathProgress.SEGMENT_DONE;
      }

      return PathProgress.SEGMENT_FAILED;
   }
}
