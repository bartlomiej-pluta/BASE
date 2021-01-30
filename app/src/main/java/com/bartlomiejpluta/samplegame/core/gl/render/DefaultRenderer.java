package com.bartlomiejpluta.samplegame.core.gl.render;

import com.bartlomiejpluta.samplegame.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.ui.Window;
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
              .createUniform(UniformName.UNI_MODEL_MATRIX)
              .createUniform(UniformName.UNI_VIEW_MATRIX)
              .createUniform(UniformName.UNI_PROJECTION_MATRIX)
              .createUniform(UniformName.UNI_OBJECT_COLOR)
              .createUniform(UniformName.UNI_HAS_OBJECT_TEXTURE)
              .createUniform(UniformName.UNI_TEXTURE_SAMPLER);
   }

   @Override
   public void render(Window window, Renderable renderable) {
      clear();
      updateViewport(window);

      shaderManager.selectShader("default").useSelectedShader();

      renderable.render(window, shaderManager);

      shaderManager.detachCurrentShader();
   }

   private void clear() {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   }

   private void updateViewport(Window window) {
      if (window.isResized()) {
         glViewport(0, 0, window.getWidth(), window.getHeight());
         window.setResized(false);
      }
   }

   @Override
   public void cleanUp() {
      shaderManager.cleanUp();
   }
}
