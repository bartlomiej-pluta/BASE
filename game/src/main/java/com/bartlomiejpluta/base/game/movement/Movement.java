package com.bartlomiejpluta.base.game.movement;

import com.bartlomiejpluta.base.api.entity.Direction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2i;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Movement {
   private final MovableSprite object;
   private final Direction direction;
   private boolean performed = false;

   public boolean perform() {
      performed = object.move(direction);
      return performed;
   }

   public Vector2i getSourceCoordinate() {
      return new Vector2i(object.getCoordinates());
   }

   public Vector2i getTargetCoordinate() {
      return new Vector2i(direction.x, direction.y).add(object.getCoordinates());
   }

   public Movement another() {
      return object.prepareMovement(direction);
   }
}