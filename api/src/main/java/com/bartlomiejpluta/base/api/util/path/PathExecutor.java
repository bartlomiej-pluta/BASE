package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.entity.Movable;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

import java.util.List;
import java.util.Objects;

public class PathExecutor<T extends Movable> {
   protected final List<PathSegment<T>> path;
   private final T movable;
   private final boolean repeat;

   private int current = 0;

   public PathExecutor(T movable, boolean repeat, Path<T> path) {
      this.movable = movable;
      this.repeat = repeat;
      this.path = Objects.requireNonNull(path).getPath();
   }

   public boolean execute(ObjectLayer layer, float dt) {
      if (!repeat && isRetired()) {
         return false;
      }

      if (!movable.isMoving()) {
         var item = path.get(current % path.size());
         if (item.perform(movable, layer, dt)) {
            ++current;
         }
      }

      return true;
   }

   private boolean isRetired() {
      return current == path.size() - 1;
   }
}
