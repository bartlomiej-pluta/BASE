package com.bartlomiejpluta.base.api.move;

import com.bartlomiejpluta.base.api.animation.Animation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Getter
@EqualsAndHashCode
public final class AnimationMovement implements Movement {
   private final Animation object;
   private final Direction direction;
   private final Vector2ic from;
   private final Vector2ic to;

   public AnimationMovement(@NonNull Animation object, @NonNull Direction direction) {
      this.object = object;
      this.direction = direction;

      this.from = object.getCoordinates();
      this.to = direction.vector.add(object.getCoordinates(), new Vector2i());
   }

   @Override
   public boolean perform() {
      return object.move(this);
   }

   @Override
   public void abort() {
      object.abortMove();
   }

   @Override
   public void onFinish() {
      // do nothing
   }
}
