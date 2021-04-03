package com.bartlomiejpluta.base.engine.world.camera;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.engine.world.object.Model;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class DefaultCamera extends Model implements Camera {
   private final Matrix4f projectionMatrix = new Matrix4f();
   private final Matrix4f viewMatrix = new Matrix4f();
   private final Matrix4f projectionViewMatrix = new Matrix4f();
   private final FrustumIntersection frustum = new FrustumIntersection();

   @Override
   public Matrix4fc computeViewModelMatrix(Matrix4fc modelMatrix) {
      return new Matrix4f(viewMatrix).mul(modelMatrix);
   }

   @Override
   public boolean insideFrustum(float x, float y, float radius) {
      return frustum.testSphere(x, y, 0.0f, radius);
   }

   @Override
   public void render(Screen screen, ShaderManager shaderManager) {
      // Update matrices
      projectionMatrix
            .identity()
            .setOrtho2D(0, screen.getWidth(), screen.getHeight(), 0);

      viewMatrix
            .identity()
            .scaleXY(scaleX, scaleY)
            .translate(-position.x, -position.y, 0);

      projectionViewMatrix
            .set(projectionMatrix)
            .mul(viewMatrix);

      frustum.set(projectionViewMatrix);

      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, projectionMatrix);
   }
}
