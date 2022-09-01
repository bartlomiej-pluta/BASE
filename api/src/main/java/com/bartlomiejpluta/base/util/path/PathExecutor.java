package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;

import java.util.List;

import static java.lang.Math.max;
import static java.util.Collections.emptyList;

public class PathExecutor<T extends Movable> {
   private final T movable;

   private List<? extends PathSegment<T>> path = emptyList();
   private Integer repeat;

   private int current = 0;
   private int iteration = 0;
   private boolean updated = false;

   public PathExecutor(T movable) {
      this.movable = movable;
   }

   public PathExecutor<T> setPath(Path<T> path) {
      this.path = path != null ? path.getPath() : emptyList();
      this.current = 0;
      this.iteration = 0;
      return this;
   }

   public PathExecutor<T> setRepeat(Integer repeat) {
      this.repeat = repeat;
      this.current = 0;
      this.iteration = 0;
      return this;
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

   public void setCurrentSegment(int segmentNumber) {
      this.current = max(0, segmentNumber - 1);
   }

   public void reset() {
      iteration = 0;
      current = 0;
   }
}
