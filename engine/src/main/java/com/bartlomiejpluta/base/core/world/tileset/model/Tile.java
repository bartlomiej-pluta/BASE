package com.bartlomiejpluta.base.core.world.tileset.model;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.world.object.RenderableObject;
import lombok.Getter;

@Getter
public class Tile extends RenderableObject {
   private final int width;
   private final int height;

   Tile(Mesh mesh, Material material, int width, int height) {
      super(mesh);
      this.width = width;
      this.height = height;
      setMaterial(material);
   }
}
