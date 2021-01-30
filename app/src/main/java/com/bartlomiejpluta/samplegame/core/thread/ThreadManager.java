package com.bartlomiejpluta.samplegame.core.thread;

import org.springframework.stereotype.Component;

@Component
public class ThreadManager {
   public Thread createThread(Runnable runnable) {
      return new Thread(runnable);
   }
}
