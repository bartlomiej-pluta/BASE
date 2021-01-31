package com.bartlomiejpluta.base.core.engine;

import com.bartlomiejpluta.base.core.logic.GameLogic;
import com.bartlomiejpluta.base.core.thread.ThreadManager;
import com.bartlomiejpluta.base.core.time.ChronoMeter;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.ui.WindowManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultGameEngine implements GameEngine {
   private static final String THREAD_NAME = "Game Main Thread";

   private final WindowManager windowManager;
   private final ThreadManager threadManager;
   private final GameLogic logic;

   private final Thread thread;
   private final Window window;
   private final ChronoMeter chrono;
   private final int targetUps;

   private boolean running = false;

   @Autowired
   public DefaultGameEngine(WindowManager windowManager,
                            ThreadManager threadManager,
                            GameLogic logic,
                            @Value("${app.window.title}") String title,
                            @Value("${app.window.width}") int width,
                            @Value("${app.window.height}") int height,
                            @Value("${app.core.targetUps}") int targetUps) {
      this.windowManager = windowManager;
      this.threadManager = threadManager;
      this.logic = logic;

      this.window = windowManager.createWindow(title, width, height);
      this.thread = threadManager.createThread(THREAD_NAME, this::run);
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
      log.info("Initializing game engine");
      window.init();
      chrono.init();
      logic.init(window);
   }

   private void loop() {
      log.info("Starting game loop");
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
      logic.input(window);
   }

   private void update(float dt) {
      logic.update(dt);
   }

   private void render() {
      window.update();
      logic.render(window);
   }

   private void cleanUp() {
      logic.cleanUp();
   }

   @Override
   public void start() {
      thread.start();
   }
}
