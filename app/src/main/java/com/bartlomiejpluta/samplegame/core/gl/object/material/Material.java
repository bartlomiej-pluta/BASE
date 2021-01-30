package com.bartlomiejpluta.samplegame.core.gl.object.material;

import com.bartlomiejpluta.samplegame.core.gl.object.texture.Texture;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector4f;

@Getter
public class Material {
   private final Vector4f color = new Vector4f();
   private final Vector2f spriteSize = new Vector2f(1, 1);
   private final Vector2f spritePosition = new Vector2f(0, 0);
   private final Texture texture;

   private Material(Texture texture, float r, float g, float b, float alpha) {
      this.texture = texture;
      setColor(r, g, b, alpha);
   }

   public void setColor(float r, float g, float b, float alpha) {
      color.x = r;
      color.y = g;
      color.z = b;
      color.w = alpha;
   }

   public void setSpriteSize(Vector2f spriteSize) {
      this.spriteSize.x = spriteSize.x;
      this.spriteSize.y = spriteSize.y;
   }

   public void setSpriteSize(float w, float h) {
      this.spriteSize.x = w;
      this.spriteSize.y = h;
   }

   public void setSpritePosition(Vector2f spritePosition) {
      this.spritePosition.x = spritePosition.x;
      this.spritePosition.y = spritePosition.y;
   }

   public void setSpritePosition(float x, float y) {
      this.spritePosition.x = x;
      this.spritePosition.y = y;
   }

   public boolean hasTexture() {
      return texture != null;
   }

   public void activateTextureIfExists() {
      if(hasTexture()) {
         texture.activate();
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
