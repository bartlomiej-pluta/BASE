package com.bartlomiejpluta.base.game.image.model;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.object.Sprite;
import lombok.Getter;

@Getter
public class Image extends Sprite {
   private final int initialWidth;
   private final int initialHeight;
   private final int gcd;

   public Image(Mesh mesh, Material texture, int initialWidth, int initialHeight, int gcd) {
      super(mesh, texture);
      this.initialWidth = initialWidth;
      this.initialHeight = initialHeight;
      this.gcd = gcd;
   }
}
