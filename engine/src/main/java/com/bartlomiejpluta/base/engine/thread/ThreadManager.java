package com.bartlomiejpluta.base.engine.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ThreadManager {
   public Thread createThread(String name, Runnable runnable) {
      log.info("Creating [{}] thread", name);
      return new Thread(runnable, name);
   }
}
