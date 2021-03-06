package com.bartlomiejpluta.base.engine.world.movement;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import lombok.Data;
import org.joml.Vector2i;

@Data
public class DefaultMovement implements Movement {
   private final MovableSprite object;
   private final Direction direction;
   private final Vector2i from;
   private final Vector2i to;
   private boolean performed = false;

   DefaultMovement(MovableSprite object, Direction direction) {
      this.object = object;
      this.direction = direction;

      this.from = object.getCoordinates();
      this.to = direction.asVector().add(object.getCoordinates());
   }

   @Override
   public boolean perform() {
      return object.move(this);
   }

   @Override
   public Movement another() {
      return object.prepareMovement(direction);
   }

   @Override
   public Vector2i getFrom() {
      return from;
   }

   @Override
   public Vector2i getTo() {
      return to;
   }
}