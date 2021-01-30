package com.bartlomiejpluta.samplegame.game.tile;

import com.bartlomiejpluta.samplegame.core.gl.object.texture.Texture;

public class TileSet {
   private static final int TILE_SIZE = 16;

   private final Texture texture;
   private final int rows;
   private final int cols;

   public TileSet(Texture texture) {
      this.texture = texture;
      this.rows = texture.getHeight() / TILE_SIZE;
      this.cols = texture.getWidth() / TILE_SIZE;
   }

   public Tile getTile(int m, int n) {
      return new Tile(texture, m, n, TILE_SIZE);
   }

   public Tile getTile(int i) {
      return new Tile(texture, i % cols, i / rows, TILE_SIZE);
   }
}
