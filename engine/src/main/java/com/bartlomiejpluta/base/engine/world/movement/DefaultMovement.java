package com.bartlomiejpluta.base.engine.world.movement;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joml.Vector2i;
import org.joml.Vector2ic;

@Getter
@EqualsAndHashCode
public class DefaultMovement implements Movement {
   private final MovableSprite object;
   private final Direction direction;
   private final Vector2ic from;
   private final Vector2ic to;

   DefaultMovement(MovableSprite object, Direction direction) {
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
   public Movement another() {
      return object.prepareMovement(direction);
   }
}