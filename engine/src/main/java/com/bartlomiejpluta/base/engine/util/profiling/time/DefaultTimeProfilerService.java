package com.bartlomiejpluta.base.engine.util.profiling.time;

import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Slf4j
@Component
public class DefaultTimeProfilerService implements TimeProfilerService, Cleanable {
   private static final DecimalFormat DF = new DecimalFormat("0.00");
   private final Map<String, Double> averages = new HashMap<>();

   @Override
   public void measure(String key, Runnable task) {
      var start = System.nanoTime();
      task.run();
      var time = System.nanoTime() - start;

      if (!averages.containsKey(key)) {
         averages.put(key, time * 1.0);
      } else {
         averages.put(key, (averages.get(key) + time) / 2.0);
      }
   }

   @Override
   public void cleanUp() {
      averages.entrySet().stream()
            .sorted(Entry.<String, Double>comparingByValue().reversed())
            .forEachOrdered(entry -> log.info("[{}]: [{}ms] [{}us] [{}ns]",
                  entry.getKey(),
                  DF.format(entry.getValue() / 1_000_000),
                  DF.format(entry.getValue() / 1_000),
                  DF.format(entry.getValue())
            ));
   }
}
