package com.bartlomiejpluta.base.api.geo;

public class Vector {
   public final int x;
   public final int y;

   public Vector(int x, int y) {
      this.x = x;
      this.y = y;
   }

   public Vector add(Vector other) {
      return new Vector(x + other.x, y + other.y);
   }

   public static Vector of(int x, int y) {
      return new Vector(x, y);
   }
}
