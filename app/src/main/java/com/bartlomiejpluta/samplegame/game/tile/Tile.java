package com.bartlomiejpluta.samplegame.game.tile;

import com.bartlomiejpluta.samplegame.core.gl.object.material.Material;
import com.bartlomiejpluta.samplegame.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.samplegame.core.gl.object.texture.Texture;
import com.bartlomiejpluta.samplegame.core.world.object.RenderableObject;
import lombok.Getter;

@Getter
public class Tile extends RenderableObject {
   private static final int[] ELEMENTS = new int[]{
           0, 1, 2,
           2, 3, 0
   };

   private final int row;
   private final int col;
   private final int size;

   Tile(Texture texture, int row, int col, int size) {
      super(buildTileMesh(texture, row, col, size));
      this.row = row;
      this.col = col;
      this.size = size;
      setMaterial(Material.textured(texture));
   }

   private static Mesh buildTileMesh(Texture texture, int row, int col, int size) {
      var vertices = getVertices(size);
      var texCoords = getTextureCoordinates(row, col, size, texture.getWidth(), texture.getHeight());
      return new Mesh(vertices, texCoords, ELEMENTS);
   }

   private static float[] getVertices(int tileSize) {
      var half = tileSize / 2;
      return new float[]{
              -half, -half,
              -half, half,
              half, half,
              half, -half
      };
   }

   private static float[] getTextureCoordinates(int row, int col, int tileSize, int textureWidth, int textureHeight) {
      return new float[]{
              (row * tileSize) / (float) textureWidth, (col * tileSize) / (float) textureHeight,
              (row * tileSize) / (float) textureWidth, ((col + 1) * tileSize) / (float) textureHeight,
              ((row + 1) * tileSize) / (float) textureWidth, ((col + 1) * tileSize) / (float) textureHeight,
              ((row + 1) * tileSize) / (float) textureWidth, (col * tileSize) / (float) textureHeight
      };
   }
}
