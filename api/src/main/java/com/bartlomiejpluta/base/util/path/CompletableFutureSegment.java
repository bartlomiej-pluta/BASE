package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Movable;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CompletableFutureSegment<T extends Movable> implements PathSegment<T> {
   private final Supplier<CompletableFuture<?>> futureSupplier;
   private CompletableFuture<?> future;

   @Override
   public PathProgress perform(T movable, ObjectLayer layer, float dt) {
      if (future == null) {
         future = futureSupplier.get();
      }

      if (future.isCancelled() || future.isCompletedExceptionally()) {
         return PathProgress.SEGMENT_FAILED;
      }

      if (future.isDone()) {
         return PathProgress.SEGMENT_DONE;
      }

      return PathProgress.ONGOING;
   }
}
