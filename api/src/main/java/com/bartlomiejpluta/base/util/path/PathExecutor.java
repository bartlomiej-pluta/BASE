package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;

import java.util.List;
import java.util.Objects;

public class PathExecutor<T extends Movable> {
   protected final List<PathSegment<T>> path;
   private final T movable;
   private final Integer repeat;

   private int current = 0;
   private int iteration = 0;
   private boolean updated = false;

   public PathExecutor(T movable, Integer repeat, Path<T> path) {
      this.movable = movable;
      this.repeat = repeat;
      this.path = Objects.requireNonNull(path).getPath();
   }

   public PathProgress execute(ObjectLayer layer, float dt) {
      var size = path.size();

      if (current == size - 1 && !updated) {
         ++iteration;
         updated = true;
      }

      if (current == size) {
         updated = false;

         if (repeat != null && iteration >= repeat) {
            return PathProgress.DONE;
         } else {
            current = 0;
            return PathProgress.REPEAT;
         }
      }

      var result = path.get(current).perform(movable, layer, dt);
      if (result == PathProgress.SEGMENT_DONE) {
         ++current;
      }

      return result;
   }
}
