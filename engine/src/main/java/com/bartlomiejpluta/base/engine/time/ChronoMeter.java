package com.bartlomiejpluta.base.engine.time;

public class ChronoMeter {
   private double latchedTime;

   public void init() {
      latchedTime = getTime();
   }

   private double getTime() {
      return System.nanoTime() / 1_000_000_000.0;
   }

   public float getElapsedTime() {
      double time = getTime();
      float elapsedTime = (float) (time - latchedTime);
      latchedTime = time;
      return elapsedTime;
   }
}
