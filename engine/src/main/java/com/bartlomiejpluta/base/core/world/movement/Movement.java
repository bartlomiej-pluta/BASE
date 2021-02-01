package com.bartlomiejpluta.base.core.world.movement;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2i;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Movement {
   private final MovableObject object;
   private final Direction direction;

   public boolean perform() {
      return object.move(direction);
   }

   public Vector2i getSourceCoordinate() {
      return new Vector2i(object.getCoordinates());
   }

   public Vector2i getTargetCoordinate() {
      return direction.asIntVector().add(object.getCoordinates());
   }
}
//@Data
//public class Movement {
//   private final MovableObject object;
//   private final Direction direction;
//   private final Vector2i source;
//   private final Vector2i target;
//
//   Movement(MovableObject object, Direction direction) {
//      this.object = object;
//      this.direction = direction;
//      this.source = new Vector2i(object.getCoordinates());
//      this.target = direction.asIntVector().add(source);
//   }
//
//   public boolean perform() {
//      return object.move(direction);
//   }
//}
