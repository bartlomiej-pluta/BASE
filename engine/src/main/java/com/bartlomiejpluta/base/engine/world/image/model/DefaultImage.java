package com.bartlomiejpluta.base.engine.world.image.model;

import com.bartlomiejpluta.base.api.game.image.Image;
import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.Getter;

@Getter
public class DefaultImage extends Sprite implements Image {
   private final int primaryWidth;
   private final int primaryHeight;
   private final int factor;
   private final int width;
   private final int height;

   public DefaultImage(Mesh mesh, Material texture, int primaryWidth, int primaryHeight, int factor) {
      super(mesh, texture);
      this.primaryWidth = primaryWidth;
      this.primaryHeight = primaryHeight;
      this.factor = factor;

      this.width = primaryWidth * factor;
      this.height = primaryHeight * factor;
   }
}
