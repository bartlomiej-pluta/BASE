package com.bartlomiejpluta.base.engine.world.map.layer.autotile;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.map.layer.autotile.AutoTileLayer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.world.autotile.model.AutoTile;
import com.bartlomiejpluta.base.engine.world.autotile.model.AutoTileSet;
import com.bartlomiejpluta.base.engine.world.map.layer.base.BaseLayer;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.NonNull;

import java.util.Arrays;

// Algorithm source: https://love2d.org/forums/viewtopic.php?t=7826
public class DefaultAutoTileLayer extends BaseLayer implements AutoTileLayer {
   private final AutoTileSet autoTileSet;
   private final AutoTile[][] layer;
   private final boolean animated;
   private final double animationDuration;
   private final boolean connect;
   private double accumulator;

   public DefaultAutoTileLayer(@NonNull GameMap map, @NonNull AutoTileSet autoTileSet, int rows, int columns, boolean animated, double animationDuration, boolean connect) {
      super(map);
      this.autoTileSet = autoTileSet;
      this.animated = animated;
      this.animationDuration = animationDuration;
      this.connect = connect;
      layer = new AutoTile[rows][columns];
      Arrays.stream(layer).forEach(tiles -> Arrays.fill(tiles, null));
   }

   public void setTile(int row, int column, int setId) {
      var tile = autoTileSet.createTile(setId);
      tile.setCoordinates(column, row);
      layer[row][column] = tile;
   }

   public void clearTile(int row, int column) {
      layer[row][column] = null;
   }

   public void recalculate(Integer setId) {
      for (int row = 0; row < map.getRows(); ++row) {
         for (int column = 0; column < map.getColumns(); ++column) {
            recalculateTile(setId, row, column);
         }
      }
   }

   private void recalculateTile(Integer setId, int row, int column) {
      switch(autoTileSet.getLayout()) {
         case LAYOUT_2X2 -> recalculateTile2x2(setId, row, column);
         case LAYOUT_2X3 -> recalculateTile2x3(setId, row, column);
      }
   }

   private void recalculateTile2x2(Integer setId, int row, int column) {
      if (layer[row][column] == null) {
         return;
      }

      var topLeft = 0;
      var topRight = 0;
      var bottomLeft = 0;
      var bottomRight = 0;

      var tile = layer[row][column];
      var centerSetId = tile.getSetId();

      if (animated) {
         tile.shiftTileSet();
      }

      // Top
      if (row > 0 && (layer[row - 1][column] != null && (connect || layer[row - 1][column].getSetId() == centerSetId))) {
         topLeft += 2;
         topRight += 1;
      }

      // Bottom
      if (row < map.getRows() - 1 && (layer[row + 1][column] != null && (connect || layer[row + 1][column].getSetId() == centerSetId))) {
         bottomLeft += 1;
         bottomRight += 2;
      }

      // Left
      if (column > 0 && (layer[row][column - 1] != null && (connect || layer[row][column - 1].getSetId() == centerSetId))) {
         topLeft += 1;
         bottomLeft += 2;
      }

      // Right
      if (column < map.getColumns() - 1 && (layer[row][column + 1] != null && (connect || layer[row][column + 1].getSetId() == centerSetId))) {
         topRight += 2;
         bottomRight += 1;
      }

      tile.regularTile(setId, topLeft, topRight, bottomLeft, bottomRight);
   }

   private void recalculateTile2x3(Integer setId, int row, int column) {
      if (layer[row][column] == null) {
         return;
      }

      var topLeft = 0;
      var topRight = 0;
      var bottomLeft = 0;
      var bottomRight = 0;

      var tile = layer[row][column];
      var centerSetId = tile.getSetId();

      if (animated) {
         tile.shiftTileSet();
      }

      // Top
      if (row > 0 && (layer[row - 1][column] != null && (connect || layer[row - 1][column].getSetId() == centerSetId))) {
         topLeft += 2;
         topRight += 1;
      }

      // Bottom
      if (row < map.getRows() - 1 && (layer[row + 1][column] != null && (connect || layer[row + 1][column].getSetId() == centerSetId))) {
         bottomLeft += 1;
         bottomRight += 2;
      }

      // Left
      if (column > 0 && (layer[row][column - 1] != null && (connect || layer[row][column - 1].getSetId() == centerSetId))) {
         topLeft += 1;
         bottomLeft += 2;
      }

      // Right
      if (column < map.getColumns() - 1 && (layer[row][column + 1] != null && (connect || layer[row][column + 1].getSetId() == centerSetId))) {
         topRight += 2;
         bottomRight += 1;
      }

      // Top left
      if (row > 0 && column > 0 && (layer[row - 1][column - 1] != null && (connect || layer[row - 1][column - 1].getSetId() == centerSetId)) && topLeft == 3) {
         topLeft = 4;
      }

      // Top right
      if (row > 0 && column < map.getColumns() - 1 && (layer[row - 1][column + 1] != null && (connect || layer[row - 1][column + 1].getSetId() == centerSetId)) && topRight == 3) {
         topRight = 4;
      }

      // Bottom left
      if (row < map.getRows() - 1 && column > 0 && (layer[row + 1][column - 1] != null && (connect || layer[row + 1][column - 1].getSetId() == centerSetId)) && bottomLeft == 3) {
         bottomLeft = 4;
      }

      // Bottom right
      if (row < map.getRows() - 1 && column < map.getColumns() - 1 && (layer[row + 1][column + 1] != null && (connect || layer[row + 1][column + 1].getSetId() == centerSetId)) && bottomRight == 3) {
         bottomRight = 4;
      }

      if (topLeft == 0 && topRight == 0 && bottomLeft == 0 && bottomRight == 0) {
         tile.islandTile(setId);
         return;
      }

      tile.regularTile(setId, topLeft, topRight, bottomLeft, bottomRight);
   }

   @Override
   public void update(float dt) {
      super.update(dt);

      if (animated) {
         accumulator += dt;

         if (accumulator > animationDuration) {
            accumulator = 0;
            recalculate(null);
         }
      }
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      for (var row : layer) {
         for (var tile : row) {
            if (tile != null) {
               tile.render(screen, camera, shaderManager);
            }
         }
      }

      super.render(screen, camera, shaderManager);
   }
}