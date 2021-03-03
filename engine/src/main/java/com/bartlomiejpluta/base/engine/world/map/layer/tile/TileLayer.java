package com.bartlomiejpluta.base.engine.world.map.layer.tile;

import com.bartlomiejpluta.base.engine.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.engine.ui.Window;
import com.bartlomiejpluta.base.engine.world.camera.Camera;
import com.bartlomiejpluta.base.engine.world.map.layer.base.Layer;
import com.bartlomiejpluta.base.engine.world.tileset.model.Tile;

import java.util.Arrays;

public class TileLayer implements Layer {
   private final Tile[][] layer;
   private final int rows;
   private final int columns;

   public TileLayer(int rows, int columns) {
      this.rows = rows;
      this.columns = columns;
      layer = new Tile[rows][columns];
      Arrays.stream(layer).forEach(tiles -> Arrays.fill(tiles, null));
   }

   public void setTile(int row, int column, Tile tile) {
      layer[row][column] = tile;

      if(tile != null) {
         tile.setCoordinates(row, column);
      }
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

   @Override
   public void update(float dt) {

   }
}
