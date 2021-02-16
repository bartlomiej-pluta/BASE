package com.bartlomiejpluta.base.core.world.camera;

import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.object.PositionableObject;
import org.joml.Matrix4f;

public class Camera extends PositionableObject {
   private final Matrix4f projectionMatrix = new Matrix4f();
   private final Matrix4f viewMatrix = new Matrix4f();

   public Matrix4f computeViewModelMatrix(Matrix4f modelMatrix) {
      return new Matrix4f(viewMatrix).mul(modelMatrix);
   }

   public void render(Window window, ShaderManager shaderManager) {
      // Update matrices
      projectionMatrix
            .identity()
            .setOrtho2D(0, window.getWidth(), window.getHeight(), 0);

      viewMatrix
            .identity()
            .translate(-position.x, -position.y, 0);

      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, projectionMatrix);
   }
}
