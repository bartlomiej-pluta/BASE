package com.bartlomiejpluta.base.engine.core.engine;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.common.init.Initializable;
import com.bartlomiejpluta.base.engine.gc.OffHeapGarbageCollector;
import com.bartlomiejpluta.base.engine.program.GameProgram;
import com.bartlomiejpluta.base.engine.thread.ThreadManager;
import com.bartlomiejpluta.base.engine.time.ChronoMeter;
import com.bartlomiejpluta.base.engine.ui.manager.ScreenManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameEngine implements GameEngine {
   private static final String THREAD_NAME = "engine";

   private final ScreenManager screenManager;
   private final ThreadManager threadManager;
   private final GameProgram program;
   private final OffHeapGarbageCollector garbageCollector;
   private final List<Initializable> initializables;

   private final ChronoMeter chrono = new ChronoMeter();

   private Thread thread;
   private Screen screen;
   private int targetUps;

   private Context context;

   @Getter
   private boolean running = false;

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

      initializables.stream()
              .peek(i -> log.info("Initializing {}", i.getClass().getSimpleName()))
              .forEach(Initializable::init);

      program.init(screen, context);
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
      program.input();
   }

   private void update(float dt) {
      program.update(dt);
   }

   private void render() {
      screen.update();
      program.render(screen);
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

   private static final int WINDOW_WIDTH = 640;
   private static final int WINDOW_HEIGHT = 480;
   public static final int TARGET_UPS = 60;
}
