package com.bartlomiejpluta.base.core.world.map;

import com.bartlomiejpluta.base.core.world.movement.Direction;
import com.bartlomiejpluta.base.core.world.movement.Movement;
import com.bartlomiejpluta.base.core.world.tileset.model.Tile;
import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class GameMap {
   private static final int LAYERS = 4;
   private final TileSet tileSet;
   private final Tile[][] map;
   private final float scale;

   private final PassageAbility[] passageMap;

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
      passageMap = new PassageAbility[rows * cols];
      Arrays.fill(passageMap, 0, rows * cols, PassageAbility.ALLOW);
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

   public void setPassageAbility(int row, int col, PassageAbility passageAbility) {
      passageMap[row * cols + col] = passageAbility;
   }

   private PassageAbility getPassageAbility(Vector2i coordinates) {
      return passageMap[coordinates.y * cols + coordinates.x];
   }

   private PassageAbility getPassageAbility(int row, int col) {
      return passageMap[row * cols + col];
   }

   public boolean isMovementPossible(Movement movement) {
      var source = movement.getSourceCoordinate();
      var target = movement.getTargetCoordinate();
      var direction = movement.getDirection();

      var isTargetReachable = switch(getPassageAbility(target)) {
         case UP_ONLY -> direction != Direction.DOWN;
         case DOWN_ONLY -> direction != Direction.UP;
         case LEFT_ONLY -> direction != Direction.RIGHT;
         case RIGHT_ONLY -> direction != Direction.LEFT;
         case BLOCK -> false;
         case ALLOW -> true;
      };

      var canMoveFromCurrentTile = switch(getPassageAbility(source)) {
         case UP_ONLY -> direction == Direction.UP;
         case DOWN_ONLY -> direction == Direction.DOWN;
         case LEFT_ONLY -> direction == Direction.LEFT;
         case RIGHT_ONLY -> direction == Direction.RIGHT;
         default -> true;
      };

      return isTargetReachable && canMoveFromCurrentTile;
   }

   public void cleanUp() {
      Arrays.stream(map).flatMap(Arrays::stream).filter(Objects::nonNull).forEach(Tile::cleanUp);
   }
}
