package com.bartlomiejpluta.base.engine.core.engine;

import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.engine.gc.OffHeapGarbageCollector;
import com.bartlomiejpluta.base.engine.logic.GameLogic;
import com.bartlomiejpluta.base.engine.thread.ThreadManager;
import com.bartlomiejpluta.base.engine.time.ChronoMeter;
import com.bartlomiejpluta.base.engine.ui.ScreenManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultGameEngine implements GameEngine {
   private static final String THREAD_NAME = "Game Main Thread";

   private final ScreenManager screenManager;
   private final ThreadManager threadManager;
   private final GameLogic logic;
   private final OffHeapGarbageCollector garbageCollector;

   private final Thread thread;
   private final Screen screen;
   private final ChronoMeter chrono;
   private final int targetUps;

   private boolean running = false;

   @Autowired
   public DefaultGameEngine(ScreenManager screenManager,
                            ThreadManager threadManager,
                            GameLogic logic,
                            OffHeapGarbageCollector garbageCollector,
                            @Value("${app.window.title}") String title,
                            @Value("${app.window.width}") int width,
                            @Value("${app.window.height}") int height,
                            @Value("${app.core.targetUps}") int targetUps) {
      this.screenManager = screenManager;
      this.threadManager = threadManager;
      this.logic = logic;
      this.garbageCollector = garbageCollector;

      this.screen = screenManager.createScreen(title, width, height);
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
      screen.init();
      chrono.init();
      logic.init(screen);
   }

   private void loop() {
      log.info("Starting game loop");
      running = true;
      var dt = 0.0f;
      var accumulator = 0.0f;
      var step = 1.0f / targetUps;

      while (running && !screen.shouldClose()) {
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
      logic.input(screen);
   }

   private void update(float dt) {
      logic.update(dt);
   }

   private void render() {
      screen.update();
      logic.render(screen);
   }

   private void cleanUp() {
      log.info("Performing off heap garbage collection");
      garbageCollector.cleanUp();
   }

   @Override
   public void start() {
      thread.start();
   }
}
