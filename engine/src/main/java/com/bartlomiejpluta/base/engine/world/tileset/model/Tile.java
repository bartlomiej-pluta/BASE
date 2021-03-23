package com.bartlomiejpluta.base.engine.world.tileset.model;

import com.bartlomiejpluta.base.engine.core.gl.object.material.Material;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.Texture;
import com.bartlomiejpluta.base.engine.world.object.Sprite;
import lombok.Getter;
import org.joml.Vector2f;
import org.joml.Vector2fc;

@Getter
public class Tile extends Sprite {
   private final int id;
   private final int tileSetRow;
   private final int tileSetColumn;
   private final Vector2f tileScale = new Vector2f(1, 1);
   private final Vector2fc tileSpriteSize;

   public Tile setCoordinates(int row, int column) {
      var stepSize = material.getTexture().getSpriteSize();
      setPosition(column * stepSize.x(), row * stepSize.y());
      return this;
   }

   public Tile(Mesh mesh, Texture tileSet, int id) {
      super(mesh, Material.textured(tileSet));
      this.id = id;
      this.tileSetRow = id / tileSet.getColumns();
      this.tileSetColumn = id % tileSet.getColumns();
      material.setSpritePosition(tileSetColumn, tileSetRow);
      tileSpriteSize = tileSet.getSpriteSize();

      super.setScale(tileSpriteSize.x() * tileScale.x, tileSpriteSize.y() * tileScale.y);
   }

   public Tile(Mesh mesh, Texture tileSet, int row, int column) {
      super(mesh, Material.textured(tileSet));
      this.tileSetRow = row;
      this.tileSetColumn = column;
      this.id = row * tileSet.getColumns() + column;
      material.setSpritePosition(tileSetColumn, tileSetRow);
      tileSpriteSize = tileSet.getSpriteSize();

      super.setScale(tileSpriteSize.x() * tileScale.x, tileSpriteSize.y() * tileScale.y);
   }

   @Override
   public void setScaleX(float scaleX) {
      this.tileScale.x = scaleX;
      super.setScaleX(tileSpriteSize.x() * scaleX);
   }

   @Override
   public void setScaleY(float scaleY) {
      this.tileScale.y = scaleY;
      super.setScaleY(tileSpriteSize.x() * scaleY);
   }

   @Override
   public void setScale(float scale) {
      this.tileScale.x = scale;
      this.tileScale.y = scale;
      super.setScale(tileSpriteSize.x() * scale, tileSpriteSize.y() * scale);
   }

   public void setScale(float scaleX, float scaleY) {
      this.tileScale.x = scaleX;
      this.tileScale.y = scaleY;
      super.setScale(tileSpriteSize.x() * scaleX, tileSpriteSize.y() * scaleY);
   }

   @Override
   public float getScaleX() {
      return tileScale.x;
   }

   @Override
   public float getScaleY() {
      return tileScale.y;
   }
}
