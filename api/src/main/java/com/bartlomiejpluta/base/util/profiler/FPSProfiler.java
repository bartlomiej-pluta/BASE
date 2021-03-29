package com.bartlomiejpluta.base.util.profiler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparingInt;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class FPSProfiler {
   private final int batchSize;
   private final List<Double> values = new LinkedList<>();
   private float fpsAccumulator = 0;
   private int pointer = 0;

   @Getter
   private double instantFPS = 0;

   private FPSProfiler(int batchSize) {
      this.batchSize = batchSize;
   }

   public void update(float dt) {
      fpsAccumulator += dt;

      if (++pointer % batchSize == 0) {
         instantFPS = pointer / fpsAccumulator;
         fpsAccumulator = 0;
         pointer = 0;

         values.add(instantFPS);
      }
   }

   public void logResult() {
      log.info("Min FPS: {}, max FPS: {}, avg FPS: {}",
            values.stream().min(Double::compareTo).orElse(-1.0),
            values.stream().max(Double::compareTo).orElse(-1.0),
            totalAverage()
      );

      printHistogram();
   }

   private double totalAverage() {
      return values.stream().reduce(0.0, Double::sum) / values.size();
   }

   private void printHistogram() {
      values
            .stream()
            .mapToInt(Double::intValue)
            .boxed()
            .collect(groupingBy(identity(), counting()))
            .entrySet()
            .stream()
            .sorted(comparingInt(Map.Entry::getKey))
            .forEach(e -> log.info("{} FPS: {}% ({} occurrences)", e.getKey(), e.getValue() * 100.0f / values.size(), e.getValue()));
   }

   public static FPSProfiler create(int batchSize) {
      return new FPSProfiler(batchSize);
   }
}
