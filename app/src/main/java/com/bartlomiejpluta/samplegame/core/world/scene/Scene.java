package com.bartlomiejpluta.samplegame.core.world.scene;

import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import com.bartlomiejpluta.samplegame.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.world.object.RenderableObject;

import java.util.ArrayList;
import java.util.List;

public class Scene implements Renderable {
   private final List<RenderableObject> objects = new ArrayList<>();

   public Scene add(RenderableObject object) {
      objects.add(object);
      return this;
   }

   @Override
   public void render(ShaderManager shaderManager) {
      for(var object : objects) {
         shaderManager.setUniform(UniformName.UNI_MODEL_MATRIX, object.getModelMatrix());
         object.render(shaderManager);
      }
   }

   @Override
   public void cleanUp() {
      objects.forEach(Renderable::cleanUp);
   }
}
