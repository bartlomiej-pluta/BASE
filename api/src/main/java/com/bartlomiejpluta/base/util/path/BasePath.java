package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.api.move.Movable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class BasePath<T extends Movable> implements Path<T> {

   @Getter
   private final List<PathSegment<T>> path = new ArrayList<>();

   public Path<T> add(PathSegment<T> segment) {
      path.add(segment);
      return this;
   }

   public Path<T> addFirst(PathSegment<T> segment) {
      path.add(0, segment);
      return this;
   }

   public Path<T> move(Direction direction) {
      path.add(new MoveSegment<>(direction));
      return this;
   }

   public Path<T> move(Direction direction, boolean ignore) {
      path.add(new MoveSegment<>(direction, ignore));
      return this;
   }

   public Path<T> wait(float seconds) {
      path.add(new WaitSegment<>(seconds));
      return this;
   }

   public Path<T> run(Runnable runnable) {
      path.add(new RunSegment<>(runnable));
      return this;
   }
}
