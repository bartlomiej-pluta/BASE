package com.bartlomiejpluta.samplegame.core.gl.object.material;

import com.bartlomiejpluta.samplegame.core.gl.object.texture.Texture;
import lombok.Getter;
import org.joml.Vector4f;

@Getter
public class Material {
   private final Vector4f color = new Vector4f();
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
