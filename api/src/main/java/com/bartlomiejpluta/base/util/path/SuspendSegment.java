package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class SuspendSegment<T extends Movable> implements PathSegment<T> {
   private final Predicate<T> predicate;

   @Override
   public PathProgress perform(T movable, ObjectLayer layer, float dt) {
      if (predicate.test(movable)) {
         return PathProgress.SEGMENT_DONE;
      }

      return PathProgress.ONGOING;
   }
}
