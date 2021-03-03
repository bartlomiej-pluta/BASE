package com.bartlomiejpluta.base.engine.util.profiling.time;

public interface TimeProfilerService {
   void measure(String key, Runnable task);
}
