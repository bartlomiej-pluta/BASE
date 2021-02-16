package com.bartlomiejpluta.base.core.world.object;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class Sprite extends Model implements Renderable {

   @NonNull
   protected final Mesh mesh;

   @NonNull
   @Setter
   protected Material material;

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_VIEW_MODEL_MATRIX, camera.computeViewModelMatrix(getModelMatrix()));
      material.render(window, camera, shaderManager);
      mesh.render(window, camera, shaderManager);
   }
}
