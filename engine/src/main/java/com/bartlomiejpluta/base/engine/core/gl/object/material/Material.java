package com.bartlomiejpluta.base.engine.core.gl.object.material;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.Texture;
import com.bartlomiejpluta.base.engine.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import org.joml.*;

public class Material implements Renderable {
   private final Vector4f color = new Vector4f();
   private final Vector2f spritePosition = new Vector2f(0, 0);

   @Getter
   private final Texture texture;

   private Material(Texture texture, float red, float green, float blue, float alpha) {
      this.texture = texture;
      setColor(red, green, blue, alpha);
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      shaderManager.setUniform(UniformName.UNI_OBJECT_COLOR, color);

      if (texture != null) {
         shaderManager.setUniform(UniformName.UNI_HAS_OBJECT_TEXTURE, true);
         shaderManager.setUniform(UniformName.UNI_SPRITE_SIZE, texture.getSpriteFragment());
         shaderManager.setUniform(UniformName.UNI_SPRITE_POSITION, spritePosition);
         shaderManager.setUniform(UniformName.UNI_TEXTURE_SAMPLER, 0);
         texture.activate();
      } else {
         shaderManager.setUniform(UniformName.UNI_HAS_OBJECT_TEXTURE, false);
      }
   }

   public Vector4fc getColor() {
      return color;
   }

   public void setColor(Vector4fc color) {
      this.color.x = color.x();
      this.color.y = color.y();
      this.color.z = color.z();
      this.color.w = color.w();
   }

   public void setColor(Vector3fc color) {
      this.color.x = color.x();
      this.color.y = color.y();
      this.color.z = color.z();
   }

   public void setColor(float red, float green, float blue, float alpha) {
      this.color.x = red;
      this.color.y = green;
      this.color.z = blue;
      this.color.w = alpha;
   }

   public void setColor(float red, float green, float blue) {
      this.color.x = red;
      this.color.y = green;
      this.color.z = blue;
   }

   public void setRed(float red) {
      this.color.x = red;
   }

   public void setGreen(float green) {
      this.color.y = green;
   }

   public void setBlue(float blue) {
      this.color.z = blue;
   }

   public void setAlpha(float alpha) {
      this.color.w = alpha;
   }

   public Vector2fc getSpritePosition() {
      return spritePosition;
   }

   public void setSpritePosition(Vector2fc spritePosition) {
      if (texture != null) {
         var size = texture.getSpriteFragment();
         this.spritePosition.x = size.x() * spritePosition.x();
         this.spritePosition.y = size.y() * spritePosition.y();
      }
   }

   public void setSpritePosition(float x, float y) {
      if (texture != null) {
         var size = texture.getSpriteFragment();
         this.spritePosition.x = size.x() * x;
         this.spritePosition.y = size.y() * y;
      }
   }

   public static Material colored(float red, float green, float blue, float alpha) {
      return new Material(null, red, green, blue, alpha);
   }

   public static Material textured(Texture texture) {
      return new Material(texture, 1, 1, 1, 1);
   }

   public static Material textured(Texture texture, float red, float green, float blue, float alpha) {
      return new Material(texture, red, green, blue, alpha);
   }

   public static Material textured(Texture texture, float alpha) {
      return new Material(texture, 1, 1, 1, alpha);
   }
}
