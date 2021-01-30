package com.bartlomiejpluta.samplegame.core.gl.object.material;

import lombok.Getter;
import org.joml.Vector4f;

@Getter
public class Material {
   private final Vector4f color = new Vector4f();

   private Material(float r, float g, float b, float alpha) {
      setColor(r, g, b, alpha);
   }

   public void setColor(float r, float g, float b, float alpha) {
      color.x = r;
      color.y = g;
      color.z = b;
      color.w = alpha;
   }

   public static Material colored(float r, float g, float b, float alpha) {
      return new Material(r, g, b, alpha);
   }
}
