package com.bartlomiejpluta.base.engine.world.autotile.model;

import com.bartlomiejpluta.base.api.camera.Camera;
import com.bartlomiejpluta.base.api.screen.Screen;
import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.internal.render.Renderable;
import com.bartlomiejpluta.base.internal.render.ShaderManager;
import lombok.Getter;
import lombok.NonNull;

public class AutoTile implements Renderable {
   private final AutoTileSet autoTileSet;
   private final AutoSubTile topLeftSubTile;
   private final AutoSubTile topRightSubTile;
   private final AutoSubTile bottomLeftSubTile;
   private final AutoSubTile bottomRightSubTile;

   @Getter
   private int setId;

   public AutoTile(@NonNull Mesh mesh, @NonNull AutoTileSet autoTileSet, int setId) {
      this.topLeftSubTile = new AutoSubTile(mesh, autoTileSet);
      this.topRightSubTile = new AutoSubTile(mesh, autoTileSet);
      this.bottomLeftSubTile = new AutoSubTile(mesh, autoTileSet);
      this.bottomRightSubTile = new AutoSubTile(mesh, autoTileSet);
      this.autoTileSet = autoTileSet;
      this.setId = setId;
   }

   public void setCoordinates(int column, int row) {
      var x = column * autoTileSet.getTileSize().x() * 2;
      var y = row * autoTileSet.getTileSize().y() * 2;

      topLeftSubTile.setPosition(x, y);
      topRightSubTile.setPosition(x + autoTileSet.getTileSize().x(), y);
      bottomLeftSubTile.setPosition(x, y + autoTileSet.getTileSize().y());
      bottomRightSubTile.setPosition(x + autoTileSet.getTileSize().x(), y + autoTileSet.getTileSize().y());
   }

   public void regularTile(Integer setId, int topLeft, int topRight, int bottomLeft, int bottomRight) {
      if (setId == null) {
         setId = this.setId;
      }

      topLeftSubTile.recalculate(autoTileSet.getTopLeftSubTiles()[setId][topLeft]);
      topRightSubTile.recalculate(autoTileSet.getTopRightSubTiles()[setId][topRight]);
      bottomLeftSubTile.recalculate(autoTileSet.getBottomLeftSubTiles()[setId][bottomLeft]);
      bottomRightSubTile.recalculate(autoTileSet.getBottomRightSubTiles()[setId][bottomRight]);
   }

   public void islandTile(Integer setId) {
      if (setId == null) {
         setId = this.setId;
      }

      topLeftSubTile.recalculate(autoTileSet.getIslandSubTiles()[setId][0]);
      topRightSubTile.recalculate(autoTileSet.getIslandSubTiles()[setId][1]);
      bottomLeftSubTile.recalculate(autoTileSet.getIslandSubTiles()[setId][2]);
      bottomRightSubTile.recalculate(autoTileSet.getIslandSubTiles()[setId][3]);
   }

   public void shiftTileSet() {
      this.setId = (setId + 1) % autoTileSet.getSetsCount();
   }

   @Override
   public void render(Screen screen, Camera camera, ShaderManager shaderManager) {
      topLeftSubTile.render(screen, camera, shaderManager);
      topRightSubTile.render(screen, camera, shaderManager);
      bottomLeftSubTile.render(screen, camera, shaderManager);
      bottomRightSubTile.render(screen, camera, shaderManager);
   }
}
