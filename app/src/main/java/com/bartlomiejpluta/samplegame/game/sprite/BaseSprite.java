package com.bartlomiejpluta.samplegame.game.sprite;

import com.bartlomiejpluta.samplegame.core.gl.object.material.Material;
import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.gl.object.texture.Texture;
import com.bartlomiejpluta.samplegame.core.world.object.RenderableObject;
import lombok.Getter;

@Getter
public class BaseSprite extends RenderableObject {
   private static final int[] ELEMENTS = new int[]{
           0, 1, 2,
           2, 3, 0
   };

   private final int row;
   private final int col;
   private final int tileWidth;
   private final int tileHeight;

   public BaseSprite(Texture texture, int row, int col, int tileWidth, int tileHeight) {
      super(buildTileMesh(texture, row, col, tileWidth, tileHeight));
      this.row = row;
      this.col = col;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      setMaterial(Material.textured(texture));
   }

   private static Mesh buildTileMesh(Texture texture, int row, int col, int tileWidth, int tileHeight) {
      var vertices = getVertices(tileWidth, tileHeight);
      var texCoords = getTextureCoordinates(row, col, tileWidth, tileHeight, texture.getWidth(), texture.getHeight());
      return new Mesh(vertices, texCoords, ELEMENTS);
   }

   private static float[] getVertices(int tileWidth, int tileHeight) {
      var halfWidth = tileWidth / 2;
      var halfHeight = tileHeight / 2;
      return new float[]{
              -halfWidth, -halfHeight,
              -halfWidth, halfHeight,
              halfWidth, halfHeight,
              halfWidth, -halfHeight
      };
   }

   private static float[] getTextureCoordinates(int col, int row, int tileWidth, int tileHeight, int textureWidth, int textureHeight) {
      return new float[]{
              (col * tileWidth) / (float) textureWidth, (row * tileHeight) / (float) textureHeight,
              (col * tileWidth) / (float) textureWidth, ((row + 1) * tileHeight) / (float) textureHeight,
              ((col + 1) * tileWidth) / (float) textureWidth, ((row + 1) * tileHeight) / (float) textureHeight,
              ((col + 1) * tileWidth) / (float) textureWidth, (row * tileHeight) / (float) textureHeight
      };
   }
}
