package com.bartlomiejpluta.base.core.profiling.time;

public interface TimeProfilerService {
   void measure(String key, Runnable task);
}
