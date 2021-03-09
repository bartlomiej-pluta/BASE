package com.bartlomiejpluta.base.engine.world.camera;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.screen.Screen;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.engine.world.object.Model;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class DefaultCamera extends Model implements Camera {
   private final Matrix4f projectionMatrix = new Matrix4f();
   private final Matrix4f viewMatrix = new Matrix4f();

   @Override
   public Matrix4fc computeViewModelMatrix(Matrix4fc modelMatrix) {
      return new Matrix4f(viewMatrix).mul(modelMatrix);
   }

   @Override
   public void render(Screen screen, ShaderManager shaderManager) {
      // Update matrices
      projectionMatrix
            .identity()
            .setOrtho2D(0, screen.getWidth(), screen.getHeight(), 0);

      viewMatrix
            .identity()
            .translate(-position.x, -position.y, 0);

      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, projectionMatrix);
   }
}
