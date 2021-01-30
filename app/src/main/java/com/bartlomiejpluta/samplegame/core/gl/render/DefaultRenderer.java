package com.bartlomiejpluta.samplegame.core.gl.render;

import com.bartlomiejpluta.samplegame.core.ui.Window;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.system.MemoryStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultRenderer implements Renderer {
   private final ShaderManager shaderManager;

   @Override
   public void init() {
      log.info("Initializing renderer");
      shaderManager.createShader("default", "/shaders/default.vs", "/shaders/default.fs");
   }

   @Override
   public void render(Window window, Renderable renderable) {
      clear();
      updateViewport(window);

      shaderManager.selectShader("default").useSelectedShader();

      renderable.render();

      shaderManager.detachCurrentShader();
   }

   private void clear() {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
   }

   private void updateViewport(Window window) {
      if(window.isResized()) {
         glViewport(0, 0, window.getWidth(), window.getHeight());
         window.setResized(false);
      }
   }

   @Override
   public void cleanUp() {
      shaderManager.cleanUp();
   }
}
