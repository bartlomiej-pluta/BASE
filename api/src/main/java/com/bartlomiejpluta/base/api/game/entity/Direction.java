package com.bartlomiejpluta.base.api.game.entity;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import static java.lang.Math.PI;
import static org.joml.Math.atan2;

public enum Direction {
   UP(0, -1),
   DOWN(0, 1),
   LEFT(-1, 0),
   RIGHT(1, 0);

   public final int x;
   public final int y;
   public final Vector2ic vector;

   Direction(int x, int y) {
      this.x = x;
      this.y = y;
      this.vector = new Vector2i(x, y);
   }

   public Direction opposite() {
      return switch (this) {
         case UP -> DOWN;
         case RIGHT -> LEFT;
         case DOWN -> UP;
         case LEFT -> RIGHT;
      };
   }

   public static Direction ofVector(Vector2ic vector) {
      // X Versor = [1, 0]
      // dot = 1 * vector.x + 0 * vector.y = vector.x
      // det = 1 * vector.y - 0 * vector.x = vector.y
      // angle = atan2(det, dot) = atan2(vector.y, vector.x)
      float angle = atan2(vector.y(), vector.x());

      if (-PI / 4 < angle && angle < PI / 4) {
         return RIGHT;
      } else if (PI / 4 <= angle && angle <= 3 * PI / 4) {
         return DOWN;
      } else if (3 * PI / 4 < angle && angle < 5 * PI / 4) {
         return LEFT;
      } else {
         return UP;
      }
   }
}
