package com.bartlomiejpluta.base.game.world.tileset.model;

import com.bartlomiejpluta.base.core.gl.object.material.Material;
import com.bartlomiejpluta.base.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.core.gl.object.texture.Texture;
import com.bartlomiejpluta.base.core.world.object.Sprite;
import lombok.Getter;

@Getter
public class Tile extends Sprite {
   private final int id;
   private final int tileSetRow;
   private final int tileSetColumn;

   public Tile setCoordinates(int row, int column) {
      var stepSize = material.getTexture().getSpriteSize();
      setPosition(column * stepSize.x, row * stepSize.y);
      return this;
   }

   public Tile(Mesh mesh, Texture tileSet, int id) {
      super(mesh, Material.textured(tileSet));
      this.id = id;
      this.tileSetRow = id / tileSet.getColumns();
      this.tileSetColumn = id % tileSet.getColumns();
      material.setSpritePosition(tileSetColumn, tileSetRow);
   }

   public Tile(Mesh mesh, Texture tileSet, int row, int column) {
      super(mesh, Material.textured(tileSet));
      this.tileSetRow = row;
      this.tileSetColumn = column;
      this.id = row * tileSet.getColumns() + column;
      material.setSpritePosition(tileSetColumn, tileSetRow);
   }
}
