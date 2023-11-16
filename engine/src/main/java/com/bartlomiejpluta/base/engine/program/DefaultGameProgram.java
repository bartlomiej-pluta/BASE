package com.bartlomiejpluta.base.engine.program;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.input.Input;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.render.Renderer;
import com.bartlomiejpluta.base.engine.ui.model.GLFWInput;
import com.bartlomiejpluta.base.engine.world.camera.DefaultCamera;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGameProgram implements GameProgram {
   private final Renderer renderer;
   private Camera camera;
   private Input input;

   private Context context;

   @Override
   public void init(Screen screen, Context context) {
      log.info("Initializing game program");

      this.context = context;

      renderer.init();

      log.info("Creating camera model");
      camera = new DefaultCamera();

      log.info("Initializing input model");
      input = new GLFWInput(screen).init();

      log.info("Initializing game context");
      context.init(screen, input, camera);
   }

   @Override
   public void input() {
      context.input(input);
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
