package com.bartlomiejpluta.base.api.move;

import com.bartlomiejpluta.base.api.character.Character;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Getter
@EqualsAndHashCode
public final class CharacterMovement implements Movement {
   private final Character object;
   private final Direction direction;
   private final Vector2ic from;
   private final Vector2ic to;

   public CharacterMovement(@NonNull Character object, @NonNull Direction direction) {
      this.object = object;
      this.direction = direction;

      this.from = object.getCoordinates();
      this.to = direction.vector.add(object.getCoordinates(), new Vector2i());
   }

   @Override
   public boolean perform() {
      var result = object.move(this);
      if (result) {
         object.getLayer().handleEvent(new MoveEvent(MoveEvent.Action.BEGIN, object, this));
      }

      return result;
   }

   @Override
   public void abort() {
      object.abortMove();
   }

   @Override
   public void onFinish() {
      object.getLayer().handleEvent(new MoveEvent(MoveEvent.Action.END, object, this));
   }
}
