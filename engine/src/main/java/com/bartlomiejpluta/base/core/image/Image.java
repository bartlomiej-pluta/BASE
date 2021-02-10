package com.bartlomiejpluta.base.core.image;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.object.RenderableObject;
import lombok.Getter;

@Getter
public class Image extends RenderableObject {
   private final int initialWidth;
   private final int initialHeight;

   Image(Mesh mesh, Material texture, int initialWidth, int initialHeight) {
      super(mesh);
      this.initialWidth = initialWidth;
      this.initialHeight = initialHeight;
      setMaterial(texture);
   }
}
