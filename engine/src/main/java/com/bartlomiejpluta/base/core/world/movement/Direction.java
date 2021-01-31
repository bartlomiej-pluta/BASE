package com.bartlomiejpluta.base.core.world.movement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector2i;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Direction {
   UP(new Vector2i(0, -1)),
   DOWN(new Vector2i(0, 1)),
   LEFT(new Vector2i(-1, 0)),
   RIGHT(new Vector2i(1, 0));

   private final Vector2i vector;

   public Vector2i asIntVector() {
      return new Vector2i(vector);
   }

   public Vector2f asFloatVector() {
      return new Vector2f(vector);
   }
}
