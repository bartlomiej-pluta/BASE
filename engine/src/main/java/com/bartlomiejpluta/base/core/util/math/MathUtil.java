package com.bartlomiejpluta.base.core.util.math;

import static java.lang.Math.max;
import static java.lang.Math.min;

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

   public static int clamp(int value, int min, int max) {
      return min(max, max(value, min));
   }

   public static float clamp(float value, float min, float max) {
      return min(max, max(value, min));
   }

   public static double clamp(double value, double min, double max) {
      return min(max, max(value, min));
   }
}
