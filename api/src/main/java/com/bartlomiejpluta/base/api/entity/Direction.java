package com.bartlomiejpluta.base.api.entity;

import org.joml.Vector2i;

public enum Direction {
   UP(0, -1),
   DOWN(0, 1),
   LEFT(-1, 0),
   RIGHT(1, 0);

   public final int x;
   public final int y;

   Direction(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public Vector2i asVector() {
      return new Vector2i(x, y);
   }
}
