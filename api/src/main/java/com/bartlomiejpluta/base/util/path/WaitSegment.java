package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;

public class WaitSegment<T extends Movable> implements PathSegment<T> {
   private final float seconds;
   private float accumulator = 0.0f;

   public WaitSegment(float seconds) {
      this.seconds = seconds;
   }

   @Override
   public PathProgress perform(T movable, ObjectLayer layer, float dt) {
      accumulator += dt;

      if (accumulator > seconds) {
         accumulator = 0.0f;
         return PathProgress.SEGMENT_DONE;
      }

      return PathProgress.ONGOING;
   }
}
