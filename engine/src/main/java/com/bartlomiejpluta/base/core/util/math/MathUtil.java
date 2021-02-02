package com.bartlomiejpluta.base.core.util.math;

public class MathUtil {
   public static int gcdEuclidean(int a, int b) {
      int x = a;
      int y = b;
      int z;

      while(y != 0) {
         z = x % y;
         x = y;
         y = z;
      }

      return x;
   }
}
