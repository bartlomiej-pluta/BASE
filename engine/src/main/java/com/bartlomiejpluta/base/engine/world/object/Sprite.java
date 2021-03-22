package com.bartlomiejpluta.base.engine.world.object;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
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
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_VIEW_MODEL_MATRIX, camera.computeViewModelMatrix(getModelMatrix()));
      material.render(screen, camera, shaderManager);
      mesh.render(screen, camera, shaderManager);
   }
}
