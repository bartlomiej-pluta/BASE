package com.bartlomiejpluta.base.game.movement;

import com.bartlomiejpluta.base.api.entity.Direction;
import com.bartlomiejpluta.base.api.entity.Movement;
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