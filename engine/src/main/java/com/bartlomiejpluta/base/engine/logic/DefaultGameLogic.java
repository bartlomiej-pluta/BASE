package com.bartlomiejpluta.base.engine.logic;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.render.Renderer;
import com.bartlomiejpluta.base.engine.world.camera.DefaultCamera;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameLogic implements GameLogic {
   private final Renderer renderer;
   private final Camera camera = new DefaultCamera();

   private Context context;

   @Override
   public void init(Screen screen, Context context) {
      this.context = context;

      log.info("Initializing game logic");
      renderer.init();

      log.info("Initializing game context");
      context.init(screen, camera);
   }

   @Override
   public void input(Screen screen) {
      context.input(screen);
   }

   @Override
   public void update(float dt) {
      context.update(dt);
   }

   @Override
   public void render(Screen screen) {
      renderer.render(screen, camera, context);
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
