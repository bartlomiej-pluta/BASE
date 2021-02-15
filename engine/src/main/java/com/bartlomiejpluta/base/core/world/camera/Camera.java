package com.bartlomiejpluta.base.core.world.camera;

import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.object.PositionableObject;
import org.joml.Matrix4f;

public class Camera extends PositionableObject {
   private final Matrix4f projectionMatrix = new Matrix4f();
   private final Matrix4f viewMatrix = new Matrix4f();

   public Matrix4f getProjectionMatrix(Window window) {
      return projectionMatrix
              .identity()
              .setOrtho2D(0, window.getWidth(), window.getHeight(), 0);
   }

   public Matrix4f getViewMatrix() {
      return viewMatrix
              .identity()
              .translate(-position.x, -position.y, 0);
   }

   public void render(Window window, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, getProjectionMatrix(window));
      shaderManager.setUniform(UniformName.UNI_VIEW_MATRIX, getViewMatrix());
   }
}
