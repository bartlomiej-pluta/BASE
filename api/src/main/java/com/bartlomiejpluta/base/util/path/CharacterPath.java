package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.character.Character;
import com.bartlomiejpluta.base.api.move.Direction;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CharacterPath<T extends Character> implements Path<T> {

   @Getter
   private final List<PathSegment<T>> path = new ArrayList<>();

   public CharacterPath<T> add(PathSegment<T> segment) {
      path.add(segment);
      return this;
   }

   public CharacterPath<T> addFirst(PathSegment<T> segment) {
      path.add(0, segment);
      return this;
   }

   public CharacterPath<T> move(Direction direction) {
      path.add(new MoveSegment<>(direction));
      return this;
   }

   public CharacterPath<T> move(Direction direction, boolean ignore) {
      path.add(new MoveSegment<>(direction, ignore));
      return this;
   }

   public CharacterPath<T> turn(Direction direction) {
      path.add(new TurnSegment<>(direction));
      return this;
   }

   public CharacterPath<T> turn(Direction direction, int newAnimationFrame) {
      path.add(new TurnSegment<>(direction, newAnimationFrame));
      return this;
   }

   public CharacterPath<T> wait(float seconds) {
      path.add(new WaitSegment<>(seconds));
      return this;
   }

   public CharacterPath<T> run(Runnable runnable) {
      path.add(new RunSegment<>(runnable));
      return this;
   }

   public CharacterPath<T> suspend(Predicate<T> predicate) {
      path.add(new SuspendSegment<>(predicate));
      return this;
   }
}
