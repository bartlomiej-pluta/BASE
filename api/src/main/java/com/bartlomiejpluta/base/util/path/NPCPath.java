package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class NPCPath<T extends NPC> implements Path<T> {

   @Getter
   private final List<PathSegment<T>> path = new ArrayList<>();

   public NPCPath<T> add(PathSegment<T> segment) {
      path.add(segment);
      return this;
   }

   public NPCPath<T> addFirst(PathSegment<T> segment) {
      path.add(0, segment);
      return this;
   }

   public NPCPath<T> move(Direction direction) {
      path.add(new MoveSegment<>(direction));
      return this;
   }

   public NPCPath<T> move(Direction direction, boolean ignore) {
      path.add(new MoveSegment<>(direction, ignore));
      return this;
   }

   public NPCPath<T> turn(Direction direction) {
      path.add(new TurnSegment<>(direction));
      return this;
   }

   public NPCPath<T> wait(float seconds) {
      path.add(new WaitSegment<>(seconds));
      return this;
   }

   public NPCPath<T> run(Runnable runnable) {
      path.add(new RunSegment<>(runnable));
      return this;
   }
}
