package com.bartlomiejpluta.base.core.world.object;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector4f;

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

   public void setAlpha(float alpha) {
      material.setAlpha(alpha);
   }

   public void setColor(Vector4f color) {
      material.setColor(color);
   }

   public void setColor(float r, float g, float b, float alpha) {
      material.setColor(r, g, b, alpha);
   }

   public void setSpriteSize(Vector2f spriteSize) {
      material.setSpriteSize(spriteSize);
   }

   public void setSpriteSize(float w, float h) {
      material.setSpriteSize(w, h);
   }

   public void setSpritePosition(Vector2f spritePosition) {
      material.setSpritePosition(spritePosition);
   }

   public void setSpritePosition(float x, float y) {
      material.setSpritePosition(x, y);
   }
}
