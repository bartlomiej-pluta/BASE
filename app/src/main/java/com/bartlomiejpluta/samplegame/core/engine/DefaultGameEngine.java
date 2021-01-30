package com.bartlomiejpluta.samplegame.core.engine;

import com.bartlomiejpluta.samplegame.core.thread.ThreadManager;
import com.bartlomiejpluta.samplegame.core.time.ChronoMeter;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import com.bartlomiejpluta.samplegame.core.ui.WindowManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultGameEngine implements GameEngine {
   private final WindowManager windowManager;
   private final ThreadManager threadManager;

   private final Thread thread;
   private final Window window;
   private final ChronoMeter chrono;
   private final int targetUps;

   private boolean running = false;

   @Autowired
   public DefaultGameEngine(WindowManager windowManager,
                            ThreadManager threadManager,
                            @Value("${app.window.title}") String title,
                            @Value("${app.window.width}") int width,
                            @Value("${app.window.height}") int height,
                            @Value("${app.core.targetUps}") int targetUps) {
      this.windowManager = windowManager;
      this.threadManager = threadManager;


      this.window = windowManager.createWindow(title, width, height);
      this.thread = threadManager.createThread(this::run);
      this.chrono = new ChronoMeter();
      this.targetUps = targetUps;
   }

   private void run() {
      try {
         init();
         loop();
      } finally {
         cleanUp();
      }
   }

   private void init() {
      window.init();
      chrono.init();
   }

   private void loop() {
      running = true;
      var dt = 0.0f;
      var accumulator = 0.0f;
      var step = 1.0f / targetUps;

      while (running && !window.shouldClose()) {
         dt = chrono.getElapsedTime();
         accumulator += dt;

         input();

         while (accumulator >= step) {
            update(dt);
            accumulator -= step;
         }

         render();
      }
   }

   private void input() {

   }

   private void update(float dt) {

   }

   private void render() {
      window.update();
   }

   private void cleanUp() {

   }

   @Override
   public void start() {
      thread.start();
   }
}
