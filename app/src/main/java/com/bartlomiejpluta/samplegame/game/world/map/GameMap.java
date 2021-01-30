package com.bartlomiejpluta.samplegame.game.world.map;

import com.bartlomiejpluta.samplegame.game.world.tileset.model.Tile;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public class GameMap {
   private static final int LAYERS = 4;
   private final Tile[][] map;
   private final float scale;

   @Getter
   private final int rows;

   @Getter
   private final int cols;


   public GameMap(int rows, int cols, float scale) {
      this.rows = rows;
      this.cols = cols;
      this.scale = scale;
      map = new Tile[LAYERS][rows * cols];
   }

   public void setTile(int layer, int row, int col, Tile tile) {
      recalculateTileGeometry(tile, row, col);
      map[layer][row * cols + col] = tile;
   }

   private void recalculateTileGeometry(Tile tile, int i, int j) {
      tile.setScale(scale);
      var size = tile.getWidth();
      var offset = size * scale;
      tile.setPosition(i * offset, j * offset);
   }

   public Tile[] getLayer(int layer) {
      return map[layer];
   }

   public void cleanUp() {
      Arrays.stream(map).flatMap(Arrays::stream).filter(Objects::nonNull).forEach(Tile::cleanUp);
   }
}
