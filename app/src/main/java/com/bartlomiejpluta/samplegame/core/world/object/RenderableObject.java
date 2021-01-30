package com.bartlomiejpluta.samplegame.core.world.object;

import com.bartlomiejpluta.samplegame.core.gl.object.material.Material;
import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.gl.render.Renderable;
import com.bartlomiejpluta.samplegame.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.samplegame.core.ui.Window;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public abstract class RenderableObject extends Object implements Renderable {
   private final Mesh mesh;

   @Getter
   @Setter
   private Material material;

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      getMaterial().activateTextureIfExists();
      mesh.render(window, shaderManager);
   }

   @Override
   public void cleanUp() {
      mesh.cleanUp();
   }
}
