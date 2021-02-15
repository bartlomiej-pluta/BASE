package com.bartlomiejpluta.base.core.world.object;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector4f;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class RenderableObject extends PositionableObject implements Renderable {
   private final Mesh mesh;

   @Getter
   @Setter
   private Material material;

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      material.activateTextureIfExists();

      shaderManager.setUniform(UniformName.UNI_MODEL_MATRIX, getModelMatrix());
      shaderManager.setUniform(UniformName.UNI_OBJECT_COLOR, material.getColor());
      shaderManager.setUniform(UniformName.UNI_HAS_OBJECT_TEXTURE, material.hasTexture());
      shaderManager.setUniform(UniformName.UNI_TEXTURE_SAMPLER, 0);
      shaderManager.setUniform(UniformName.UNI_SPRITE_SIZE, material.getSpriteSize());
      shaderManager.setUniform(UniformName.UNI_SPRITE_POSITION, material.getSpritePosition());

      mesh.render(window, camera, shaderManager);
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
