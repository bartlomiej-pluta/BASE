package com.bartlomiejpluta.base.util.math;

import org.joml.Vector2dc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

import static java.lang.Math.*;

public class Distance {

   public static int chebyshev(int ax, int ay, int bx, int by) {
      return max(abs(ax - bx), abs(ay - by));
   }

   public static float chebyshev(float ax, float ay, float bx, float by) {
      return max(abs(ax - bx), abs(ay - by));
   }

   public static double chebyshev(double ax, double ay, double bx, double by) {
      return max(abs(ax - bx), abs(ay - by));
   }

   public static int chebyshev(Vector2ic a, Vector2ic b) {
      return max(abs(a.x() - b.x()), abs(a.y() - b.y()));
   }

   public static float chebyshev(Vector2fc a, Vector2fc b) {
      return max(abs(a.x() - b.x()), abs(a.y() - b.y()));
   }

   public static double chebyshev(Vector2dc a, Vector2dc b) {
      return max(abs(a.x() - b.x()), abs(a.y() - b.y()));
   }

   public static int manhattan(int ax, int ay, int bx, int by) {
      return abs(ax - bx) + abs(ay - by);
   }

   public static float manhattan(float ax, float ay, float bx, float by) {
      return abs(ax - bx) + abs(ay - by);
   }

   public static double manhattan(double ax, double ay, double bx, double by) {
      return abs(ax - bx) + abs(ay - by);
   }

   public static int manhattan(Vector2ic a, Vector2ic b) {
      return abs(a.x() - b.x()) + abs(a.y() - b.y());
   }

   public static float manhattan(Vector2fc a, Vector2fc b) {
      return abs(a.x() - b.x()) + abs(a.y() - b.y());
   }

   public static double manhattan(Vector2dc a, Vector2dc b) {
      return abs(a.x() - b.x()) + abs(a.y() - b.y());
   }

   public static double euclidean(float ax, float ay, float bx, float by) {
      return sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));
   }

   public static double euclidean(double ax, double ay, double bx, double by) {
      return sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));
   }

   public static double euclidean(Vector2fc a, Vector2fc b) {
      return sqrt((a.x() - b.x()) * (a.x() - b.x()) + (a.y() - b.y()) * (a.y() - b.y()));
   }

   public static double euclidean(Vector2dc a, Vector2dc b) {
      return sqrt((a.x() - b.x()) * (a.x() - b.x()) + (a.y() - b.y()) * (a.y() - b.y()));
   }
}
