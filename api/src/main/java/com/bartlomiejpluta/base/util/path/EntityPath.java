package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class EntityPath<T extends Entity> implements Path<T> {

   @Getter
   private final List<PathSegment<T>> path = new ArrayList<>();

   public EntityPath<T> add(PathSegment<T> segment) {
      path.add(segment);
      return this;
   }

   public EntityPath<T> addFirst(PathSegment<T> segment) {
      path.add(0, segment);
      return this;
   }

   public EntityPath<T> move(Direction direction) {
      path.add(new MoveSegment<>(direction));
      return this;
   }

   public EntityPath<T> move(Direction direction, boolean ignore) {
      path.add(new MoveSegment<>(direction, ignore));
      return this;
   }

   public EntityPath<T> turn(Direction direction) {
      path.add(new TurnSegment<>(direction));
      return this;
   }

   public EntityPath<T> turn(Direction direction, int newAnimationFrame) {
      path.add(new TurnSegment<>(direction, newAnimationFrame));
      return this;
   }

   public EntityPath<T> wait(float seconds) {
      path.add(new WaitSegment<>(seconds));
      return this;
   }

   public EntityPath<T> run(Runnable runnable) {
      path.add(new RunSegment<>(runnable));
      return this;
   }
}
