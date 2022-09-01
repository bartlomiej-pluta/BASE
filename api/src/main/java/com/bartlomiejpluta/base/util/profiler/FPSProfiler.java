package com.bartlomiejpluta.base.util.profiler;

import com.bartlomiejpluta.base.util.collection.LimitedQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FPSProfiler {
   private final int batchSize;

   @Getter
   private final List<Double> samples;
   private float fpsAccumulator = 0;
   private int pointer = 0;

   @Getter
   private double instantFPS = 0;

   private FPSProfiler(int batchSize, int samples) {
      this.batchSize = batchSize;
      this.samples = new LimitedQueue<>(samples);
      this.samples.add(0.0);
   }

   public static FPSProfiler create(int batchSize, int samples) {
      return new FPSProfiler(batchSize, samples);
   }

   public void update(float dt) {
      fpsAccumulator += dt;

      if (++pointer % batchSize == 0) {
         instantFPS = pointer / fpsAccumulator;
         fpsAccumulator = 0;
         pointer = 0;

         samples.add(instantFPS);
      }
   }
}