package com.bartlomiejpluta.base.engine.world.movement;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2i;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultMovement implements Movement {
   private final MovableSprite object;
   private final Direction direction;
   private boolean performed = false;

   @Override
   public boolean perform() {
      performed = object.move(direction);
      return performed;
   }

   @Override
   public Movement another() {
      return object.prepareMovement(direction);
   }

   @Override
   public Vector2i getFrom() {
      return object.getCoordinates();
   }

   @Override
   public Vector2i getTo() {
      return direction.asVector().add(object.getCoordinates());
   }
}