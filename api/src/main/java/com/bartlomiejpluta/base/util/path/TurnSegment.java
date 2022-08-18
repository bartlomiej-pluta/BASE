package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import lombok.NonNull;

import static java.util.Objects.requireNonNull;

public class TurnSegment<T extends Entity> implements PathSegment<T> {
   private final Direction direction;
   private final Integer newAnimationFrame;

   public TurnSegment(@NonNull Direction direction, int newAnimationFrame) {
      this.direction = direction;
      this.newAnimationFrame = newAnimationFrame;
   }

   public TurnSegment(@NonNull Direction direction) {
      this.direction = direction;
      this.newAnimationFrame = null;
   }

   @Override
   public PathProgress perform(T entity, ObjectLayer layer, float dt) {
      entity.setFaceDirection(direction);
      if (newAnimationFrame != null) {
         entity.setAnimationFrame(newAnimationFrame);
      }
      return PathProgress.SEGMENT_DONE;
   }
}
