package com.bartlomiejpluta.base.engine.world.object;

import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.render.Renderable;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.engine.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.engine.ui.Window;
import com.bartlomiejpluta.base.engine.world.camera.Camera;
import lombok.*;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class Sprite extends Model implements Renderable {

   @NonNull
   protected final Mesh mesh;

   @NonNull
   @Setter
   @Getter
   protected Material material;

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_VIEW_MODEL_MATRIX, camera.computeViewModelMatrix(getModelMatrix()));
      material.render(window, camera, shaderManager);
      mesh.render(window, camera, shaderManager);
   }
}
