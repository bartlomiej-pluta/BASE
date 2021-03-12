package com.bartlomiejpluta.base.engine.core.engine;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.engine.gc.OffHeapGarbageCollector;
import com.bartlomiejpluta.base.engine.logic.GameLogic;
import com.bartlomiejpluta.base.engine.thread.ThreadManager;
import com.bartlomiejpluta.base.engine.time.ChronoMeter;
import com.bartlomiejpluta.base.engine.ui.ScreenManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameEngine implements GameEngine {
   private static final String THREAD_NAME = "engine";

   private final ScreenManager screenManager;
   private final ThreadManager threadManager;
   private final GameLogic logic;
   private final OffHeapGarbageCollector garbageCollector;

   private final ChronoMeter chrono = new ChronoMeter();

   private Thread thread;
   private Screen screen;
   private int targetUps;

   private Context context;

   @Getter
   private boolean running = false;

   @Getter
   private boolean paused = true;

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
      logic.init(screen, context);
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

         if (!paused) {
            while (accumulator >= step) {
               update(dt);
               accumulator -= step;
            }
         } else {
            accumulator = 0;
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

      log.info("Disposing context");
      context.dispose();
   }

   @Override
   public void start(Context context) {
      this.context = context;

      this.screen = screenManager.createScreen(context.getProjectName(), WINDOW_WIDTH, WINDOW_HEIGHT);
      this.thread = threadManager.createThread(THREAD_NAME, this::run);

      this.targetUps = TARGET_UPS;

      log.info("Starting [{}] thread", THREAD_NAME);
      thread.start();
   }

   @Override
   public void stop() {
      log.info("Stopping the engine");
      running = false;
   }

   @Override
   public void pause() {
      log.info("Pausing the engine");
      paused = true;
   }

   @Override
   public void resume() {
      log.info("Resuming the engine");
      paused = false;
   }

   @Override
   public boolean togglePaused() {
      paused = !paused;

      log.info("{} the engine", paused ? "Pausing" : "Resuming");

      return paused;
   }

   // TODO
   // It is supposed to be moved to the Context so that
   // user will be able to choose default window size,
   // as well as further possibility to resize the window
   // or forcing fullscreen mode.
   // Until it's not implemented yet, the window width and height
   // are hardcoded right here.
   //
   // The same applies for target UPS (updates per second) parameter.
   private static final int WINDOW_WIDTH = 640;
   private static final int WINDOW_HEIGHT = 480;
   private static final int TARGET_UPS = 60;
}
