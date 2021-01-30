package com.bartlomiejpluta.samplegame.core.world.object;

import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RenderableObject extends Object implements Renderable {
   private final Mesh mesh;

   @Override
   public void render(ShaderManager shaderManager) {
      mesh.render(shaderManager);
   }

   @Override
   public void cleanUp() {
      mesh.cleanUp();
   }
}
