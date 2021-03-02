package com.bartlomiejpluta.base.api.entity;

import com.bartlomiejpluta.base.api.geo.Vector;

public enum Direction {
   UP(0, -1),
   DOWN(0, 1),
   LEFT(-1, 0),
   RIGHT(1, 0);

   public final int x;
   public final int y;
   public final Vector vector;

   Direction(int x, int y) {
      this.x = x;
      this.y = y;
      this.vector = Vector.of(x, y);
   }
}
