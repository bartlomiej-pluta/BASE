package com.bartlomiejpluta.base.engine.world.map.layer.tile;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.map.layer.tile.TileLayer;
import com.bartlomiejpluta.base.api.map.model.GameMap;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.world.map.layer.base.BaseLayer;
import com.bartlomiejpluta.base.engine.world.tileset.model.Tile;
import com.bartlomiejpluta.base.engine.world.tileset.model.TileSet;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.NonNull;

import java.util.Arrays;

public class DefaultTileLayer extends BaseLayer implements TileLayer {
   private final TileSet tileSet;
   private final Tile[][] layer;

   public DefaultTileLayer(@NonNull GameMap map, @NonNull TileSet tileSet, int rows, int columns) {
      super(map);
      this.tileSet = tileSet;
      layer = new Tile[rows][columns];
      Arrays.stream(layer).forEach(tiles -> Arrays.fill(tiles, null));
   }

   @Override
   public void setTile(int row, int column, int tileId) {
      var tile = tileSet.tileById(tileId);
      tile.setLocation(row, column);
      layer[row][column] = tile;

   }

   @Override
   public void setTile(int row, int column, int tileSetRow, int tileSetColumn) {
      var tile = tileSet.tileAt(tileSetRow, tileSetColumn);
      tile.setLocation(row, column);
      layer[row][column] = tile;
   }

   @Override
   public void clearTile(int row, int column) {
      layer[row][column] = null;
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
