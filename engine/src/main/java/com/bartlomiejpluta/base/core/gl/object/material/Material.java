package com.bartlomiejpluta.base.core.gl.object.material;

import com.bartlomiejpluta.base.core.gl.object.texture.Texture;
import com.bartlomiejpluta.base.core.gl.render.Renderable;
import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.camera.Camera;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector4f;

@Getter
public class Material implements Renderable {
   private final Vector4f color = new Vector4f();
   private final Vector2f spritePosition = new Vector2f(0, 0);
   private final Texture texture;

   private Material(Texture texture, float r, float g, float b, float alpha) {
      this.texture = texture;
      setColor(r, g, b, alpha);
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_OBJECT_COLOR, color);

      if(texture != null) {
         shaderManager.setUniform(UniformName.UNI_HAS_OBJECT_TEXTURE, true);
         shaderManager.setUniform(UniformName.UNI_SPRITE_SIZE, texture.getSpriteFragment());
         shaderManager.setUniform(UniformName.UNI_SPRITE_POSITION, spritePosition);
         shaderManager.setUniform(UniformName.UNI_TEXTURE_SAMPLER, 0);
         texture.activate();
      } else {
         shaderManager.setUniform(UniformName.UNI_HAS_OBJECT_TEXTURE, false);
      }
   }

   public void setAlpha(float alpha) {
      this.color.w = alpha;
   }

   public void setColor(Vector4f color) {
      this.color.x = color.x;
      this.color.y = color.y;
      this.color.z = color.z;
      this.color.w = color.w;
   }

   public void setColor(float r, float g, float b, float alpha) {
      color.x = r;
      color.y = g;
      color.z = b;
      color.w = alpha;
   }

   public void setSpritePosition(Vector2f spritePosition) {
      if(texture != null) {
         var size = texture.getSpriteFragment();
         this.spritePosition.x = size.x * spritePosition.x;
         this.spritePosition.y = size.y * spritePosition.y;
      }
   }

   public void setSpritePosition(float x, float y) {
      if(texture != null) {
         var size = texture.getSpriteFragment();
         this.spritePosition.x = size.x * x;
         this.spritePosition.y = size.y * y;
      }
   }

   public static Material colored(float r, float g, float b, float alpha) {
      return new Material(null, r, g, b, alpha);
   }

   public static Material textured(Texture texture) {
      return new Material(texture, 1, 1, 1, 1);
   }

   public static Material textured(Texture texture, float r, float g, float b, float alpha) {
      return new Material(texture, r, g, b, alpha);
   }

   public static Material textured(Texture texture, float alpha) {
      return new Material(texture, 1, 1, 1, alpha);
   }
}
