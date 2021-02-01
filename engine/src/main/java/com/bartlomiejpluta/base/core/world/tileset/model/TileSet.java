package com.bartlomiejpluta.base.core.world.tileset.model;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.object.texture.Texture;
import lombok.Getter;

public class TileSet {
   private final Texture texture;
   private final int rows;
   private final int columns;
   private final float columnStep;
   private final float rowStep;
   private final Mesh mesh;

   @Getter
   private final int tileWidth;

   @Getter
   private final int tileHeight;

   public TileSet(Mesh mesh, Texture texture, int rows, int columns, int tileWidth, int tileHeight) {
      this.texture = texture;
      this.rows = rows;
      this.columns = columns;
      this.columnStep = 1/(float) columns;
      this.rowStep = 1/(float) rows;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.mesh = mesh;
   }

   public Tile getTile(int row, int column) {
      var material = Material.textured(texture);
      material.setSpriteSize(columnStep, rowStep);
      material.setSpritePosition(column * columnStep, row * rowStep);
      return new Tile(mesh, material, tileWidth, tileHeight);
   }
}
