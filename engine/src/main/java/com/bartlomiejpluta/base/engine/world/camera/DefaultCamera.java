package com.bartlomiejpluta.base.engine.world.camera;

import com.bartlomiejpluta.base.api.internal.camera.Camera;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.api.internal.window.Window;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.engine.world.object.Model;
import org.joml.Matrix4f;

public class DefaultCamera extends Model implements Camera {
   private final Matrix4f projectionMatrix = new Matrix4f();
   private final Matrix4f viewMatrix = new Matrix4f();

   @Override
   public Matrix4f computeViewModelMatrix(Matrix4f modelMatrix) {
      return new Matrix4f(viewMatrix).mul(modelMatrix);
   }

   @Override
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
