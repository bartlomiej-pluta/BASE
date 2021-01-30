package com.bartlomiejpluta.samplegame.core.world.object;

import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RenderableObject extends Object implements Renderable {
   private final Mesh mesh;

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      mesh.render(window, shaderManager);
   }

   @Override
   public void cleanUp() {
      mesh.cleanUp();
   }
}
