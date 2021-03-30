package com.bartlomiejpluta.base.api.move;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Getter
@EqualsAndHashCode
public final class Movement {
   private final Movable object;
   private final Direction direction;
   private final Vector2ic from;
   private final Vector2ic to;

   public Movement(@NonNull Movable object, @NonNull Direction direction) {
      this.object = object;
      this.direction = direction;

      this.from = object.getCoordinates();
      this.to = direction.vector.add(object.getCoordinates(), new Vector2i());
   }

   public boolean perform() {
      return object.move(this);
   }

   public void abort() {
      object.abortMove();
   }

   public Movement another() {
      return object.prepareMovement(direction);
   }
}
