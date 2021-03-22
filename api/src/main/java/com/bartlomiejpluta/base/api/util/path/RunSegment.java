package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.game.move.Movable;

import static java.util.Objects.requireNonNull;

public class RunSegment<T extends Movable> implements PathSegment<T> {
   private final Runnable runnable;

   public RunSegment(Runnable runnable) {
      this.runnable = requireNonNull(runnable);
   }

   @Override
   public PathProgress perform(T movable, ObjectLayer layer, float dt) {
      runnable.run();
      return PathProgress.SEGMENT_DONE;
   }
}
