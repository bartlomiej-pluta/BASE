package com.bartlomiejpluta.base.core.world.scene;

import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import com.bartlomiejpluta.base.core.world.object.RenderableObject;
import com.bartlomiejpluta.base.core.world.map.GameMap;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class Scene implements Renderable {
   private final Camera camera;

   @Setter
   private GameMap map;

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, camera.getProjectionMatrix(window));
      shaderManager.setUniform(UniformName.UNI_VIEW_MATRIX, camera.getViewMatrix());

      renderArray(map.getLayer(0), window, shaderManager);
      renderArray(map.getLayer(1), window, shaderManager);

      // Player will be rendered here

      renderArray(map.getLayer(2), window, shaderManager);
      renderArray(map.getLayer(3), window, shaderManager);
   }

   private <T extends RenderableObject> void renderArray(T[] objects, Window window, ShaderManager shaderManager) {
      for (var object : objects) {
         if (object != null) {
            renderObject(object, window, shaderManager);
         }
      }
   }

   private <T extends RenderableObject> void renderObject(T object, Window window, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_MODEL_MATRIX, object.getModelMatrix());
      shaderManager.setUniform(UniformName.UNI_OBJECT_COLOR, object.getMaterial().getColor());
      shaderManager.setUniform(UniformName.UNI_HAS_OBJECT_TEXTURE, object.getMaterial().hasTexture());
      shaderManager.setUniform(UniformName.UNI_TEXTURE_SAMPLER, 0);
      shaderManager.setUniform(UniformName.UNI_SPRITE_SIZE, object.getMaterial().getSpriteSize());
      shaderManager.setUniform(UniformName.UNI_SPRITE_POSITION, object.getMaterial().getSpritePosition());

      object.render(window, shaderManager);
   }

   @Override
   public void cleanUp() {
      map.cleanUp();
   }
}
