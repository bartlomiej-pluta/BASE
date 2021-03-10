package com.bartlomiejpluta.base.engine.core.gl.render;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.Renderable;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.lwjgl.opengl.GL15.*;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultRenderer implements Renderer {
   private final ShaderManager shaderManager;

   @Override
   public void init() {
      log.info("Initializing renderer");
      shaderManager
            .createShader("default", "/shaders/default.vs", "/shaders/default.fs")
            .selectShader("default")
            .createUniform(UniformName.UNI_VIEW_MODEL_MATRIX)
            .createUniform(UniformName.UNI_PROJECTION_MATRIX)
            .createUniform(UniformName.UNI_OBJECT_COLOR)
            .createUniform(UniformName.UNI_HAS_OBJECT_TEXTURE)
            .createUniform(UniformName.UNI_TEXTURE_SAMPLER)
            .createUniform(UniformName.UNI_SPRITE_SIZE)
            .createUniform(UniformName.UNI_SPRITE_POSITION);
   }

   @Override
   public void render(Screen screen, Camera camera, Renderable renderable) {
      clear();
      updateViewport(screen);

      shaderManager.selectShader("default").useSelectedShader();

      // Important note:
      // The camera render method must be invoked **before** each consecutive item renders
      // due to the fact, that the method updates projection and view matrices, that
      // are used to compute proper vertex coordinates of rendered objects (renderables).
      camera.render(screen, shaderManager);
      renderable.render(screen, camera, shaderManager);

      shaderManager.detachCurrentShader();

      screen.restoreState();
   }

   private void clear() {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
   }

   private void updateViewport(Screen screen) {
      if (screen.isResized()) {
         glViewport(0, 0, screen.getWidth(), screen.getHeight());
         screen.setResized(false);
      }
   }

   @Override
   public void cleanUp() {
      log.info("There is nothing to clean up here");
   }
}
