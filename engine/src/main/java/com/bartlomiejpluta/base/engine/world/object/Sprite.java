package com.bartlomiejpluta.base.engine.world.object;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.engine.world.location.LocationableModel;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
public abstract class Sprite extends LocationableModel implements Renderable {
   private final float farthestVertexDistance;

   protected final Mesh mesh;

   @NonNull
   @Setter
   @Getter
   protected Material material;

   public Sprite(Mesh mesh, Material material) {
      this.mesh = mesh;
      this.material = material;

      this.farthestVertexDistance = this.mesh.getFarthestVertex().lengthSquared();
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      if (!camera.insideFrustum(position.x, position.y, farthestVertexDistance * (scaleX > scaleY ? scaleX : scaleY))) {
         return;
      }

      shaderManager.setUniform(UniformName.UNI_VIEW_MODEL_MATRIX, camera.computeViewModelMatrix(getModelMatrix()));
      material.render(screen, camera, shaderManager);
      mesh.render(screen, camera, shaderManager);
   }
}
