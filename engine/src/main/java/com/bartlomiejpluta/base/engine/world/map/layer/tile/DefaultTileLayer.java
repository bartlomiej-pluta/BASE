package com.bartlomiejpluta.base.engine.world.map.layer.tile;

import com.bartlomiejpluta.base.api.game.camera.Camera;
import com.bartlomiejpluta.base.api.game.map.TileLayer;
import com.bartlomiejpluta.base.api.game.window.Window;
import com.bartlomiejpluta.base.api.internal.render.ShaderManager;
import com.bartlomiejpluta.base.engine.world.tileset.model.Tile;
import com.bartlomiejpluta.base.engine.world.tileset.model.TileSet;
import lombok.NonNull;

import java.util.Arrays;

public class DefaultTileLayer implements TileLayer {
   private final TileSet tileSet;
   private final Tile[][] layer;

   public DefaultTileLayer(@NonNull TileSet tileSet, int rows, int columns) {
      this.tileSet = tileSet;
      layer = new Tile[rows][columns];
      Arrays.stream(layer).forEach(tiles -> Arrays.fill(tiles, null));
   }

   @Override
   public void setTile(int row, int column, int tileId) {
      var tile = tileSet.tileById(tileId);
      tile.setCoordinates(row, column);
      layer[row][column] = tile;

   }

   @Override
   public void setTile(int row, int column, int tileSetRow, int tileSetColumn) {
      var tile = tileSet.tileAt(tileSetRow, tileSetColumn);
      tile.setCoordinates(row, column);
      layer[row][column] = tile;
   }

   @Override
   public void clearTile(int row, int column) {
      layer[row][column] = null;
   }

   @Override
   public void update(float dt) {
      // Do nothing
   }

   @Override
   public void render(Window window, Camera camera, ShaderManager shaderManager) {
      for (var row : layer) {
         for (var tile : row) {
            if (tile != null) {
               tile.render(window, camera, shaderManager);
            }
         }
      }
   }
}
