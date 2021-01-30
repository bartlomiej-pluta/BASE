package com.bartlomiejpluta.samplegame.core.world.scene;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import com.bartlomiejpluta.samplegame.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import com.bartlomiejpluta.samplegame.core.world.camera.Camera;
import com.bartlomiejpluta.samplegame.core.world.object.RenderableObject;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Scene implements Renderable {
   private final Camera camera;
   private final List<RenderableObject> objects = new ArrayList<>();

   public Scene add(RenderableObject object) {
      objects.add(object);
      return this;
   }

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_PROJECTION_MATRIX, camera.getProjectionMatrix(window));
      shaderManager.setUniform(UniformName.UNI_VIEW_MATRIX, camera.getViewMatrix());

      for(var object : objects) {
         shaderManager.setUniform(UniformName.UNI_MODEL_MATRIX, object.getModelMatrix());
         shaderManager.setUniform(UniformName.UNI_OBJECT_COLOR, object.getMaterial().getColor());
         object.render(window, shaderManager);
      }
   }

   @Override
   public void cleanUp() {
      objects.forEach(Renderable::cleanUp);
   }
}
