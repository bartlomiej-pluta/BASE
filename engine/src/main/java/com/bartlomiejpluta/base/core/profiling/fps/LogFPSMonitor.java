package com.bartlomiejpluta.base.core.profiling.fps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparingInt;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Component
public class LogFPSMonitor implements FPSMonitor {
   private static final int MOD = 30;
   private final List<Double> values = new LinkedList<>();
   private float fpsAccumulator = 0;
   private int pointer = 0;
   private double fps = 0;

   @Override
   public void update(float dt) {
      fpsAccumulator += dt;

      if (++pointer % MOD == 0) {
         fps = pointer / fpsAccumulator;
         fpsAccumulator = 0;
         pointer = 0;

         values.add(fps);
      }
   }

   @Override
   public void cleanUp() {
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
}