package com.bartlomiejpluta.base.core.world.map;

import com.bartlomiejpluta.base.core.gl.shader.constant.UniformName;
import com.bartlomiejpluta.base.core.gl.shader.manager.ShaderManager;
import com.bartlomiejpluta.base.core.ui.Window;
import com.bartlomiejpluta.base.core.world.tileset.model.Tile;
import org.joml.Vector2f;

public class TileLayer implements Layer {
   private final Tile[][] layer;
   private final Vector2f stepSize;
   private final float scale;

   public TileLayer(Tile[][] layer, Vector2f stepSize, float scale) {
      this.layer = layer;
      this.stepSize = stepSize;
      this.scale = scale;
   }

   public void setTile(int row, int column, Tile tile) {
      layer[row][column] = tile;
      recalculateTileGeometry(tile, row, column);
   }

   private void recalculateTileGeometry(Tile tile, int row, int column) {
      tile.setScale(scale);
      tile.setPosition(column * stepSize.x, row * stepSize.y);
   }

   @Override
   public void render(Window window, ShaderManager shaderManager) {
      for(var row : layer) {
         for(var tile : row) {
            shaderManager.setUniform(UniformName.UNI_MODEL_MATRIX, tile.getModelMatrix());
            tile.render(window, shaderManager);
         }
      }
   }

   @Override
   public void update(float dt) {

   }
}
