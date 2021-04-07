package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;

import static java.util.Objects.requireNonNull;

public class TurnSegment<T extends Entity> implements PathSegment<T> {
   private final Direction direction;

   public TurnSegment(Direction direction) {
      this.direction = requireNonNull(direction);
   }

   @Override
   public PathProgress perform(T entity, ObjectLayer layer, float dt) {
      entity.setFaceDirection(direction);
      return PathProgress.SEGMENT_DONE;
   }
}
