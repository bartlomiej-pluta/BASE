package com.bartlomiejpluta.base.core.world.map;

import com.bartlomiejpluta.base.core.world.tileset.model.Tile;
import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class GameMap {
   private static final int LAYERS = 4;
   private final TileSet tileSet;
   private final Tile[][] map;
   private final float scale;

   @Getter
   private final Vector2f stepSize;

   @Getter
   private final int rows;

   @Getter
   private final int cols;

   public GameMap(TileSet tileSet, int rows, int cols, float scale) {
      this.tileSet = tileSet;
      this.rows = rows;
      this.cols = cols;
      this.scale = scale;
      this.stepSize = new Vector2f(this.scale * this.tileSet.getTileWidth(), this.scale * this.tileSet.getTileHeight());

      map = new Tile[LAYERS][rows * cols];
   }

   public void setTile(int layer, int row, int col, Tile tile) {
      recalculateTileGeometry(tile, row, col);
      map[layer][row * cols + col] = tile;
   }

   private void recalculateTileGeometry(Tile tile, int i, int j) {
      tile.setScale(scale);
      tile.setPosition(i * stepSize.x, j * stepSize.y);
   }

   public Tile[] getLayer(int layer) {
      return map[layer];
   }

   public void cleanUp() {
      Arrays.stream(map).flatMap(Arrays::stream).filter(Objects::nonNull).forEach(Tile::cleanUp);
   }
}
