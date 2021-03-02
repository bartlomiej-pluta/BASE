package com.bartlomiejpluta.base.game.movement;

import com.bartlomiejpluta.base.api.entity.Direction;
import com.bartlomiejpluta.base.api.entity.Movement;
import com.bartlomiejpluta.base.api.geo.Vector;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
   public Vector getFrom() {
      return object.getCoordinates();
   }

   @Override
   public Vector getTo() {
      return getFrom().add(direction.vector);
   }
}