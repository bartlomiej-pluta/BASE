package com.bartlomiejpluta.base.engine.world.autotile.model;

import com.bartlomiejpluta.base.engine.core.gl.object.mesh.Mesh;
import com.bartlomiejpluta.base.engine.core.gl.object.texture.Texture;
import lombok.Getter;
import lombok.NonNull;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.LinkedList;

// Algorithm source: https://love2d.org/forums/viewtopic.php?t=7826
public class AutoTileSet {
   private static final Vector2ic ISLAND_TILE_2x3 = new Vector2i(0, 0);
   private static final Vector2ic CROSS_TILE_2x3 = new Vector2i(1, 0);
   private static final Vector2ic TOP_LEFT_TILE_2x3 = new Vector2i(0, 1);
   private static final Vector2ic TOP_RIGHT_TILE_2x3 = new Vector2i(1, 1);
   private static final Vector2ic BOTTOM_LEFT_TILE_2x3 = new Vector2i(0, 2);
   private static final Vector2ic BOTTOM_RIGHT_TILE_2x3 = new Vector2i(1, 2);

   private static final Vector2ic TOP_LEFT_TILE_2x2 = new Vector2i(0, 0);
   private static final Vector2ic TOP_RIGHT_TILE_2x2 = new Vector2i(1, 0);
   private static final Vector2ic BOTTOM_LEFT_TILE_2x2 = new Vector2i(0, 1);
   private static final Vector2ic BOTTOM_RIGHT_TILE_2x2 = new Vector2i(1, 1);

   private final int textureSubTilesRows;
   private final int textureSubTilesColumns;


   @Getter
   private Vector2ic[][] islandSubTiles;

   @Getter
   private Vector2ic[][] topLeftSubTiles;

   @Getter
   private Vector2ic[][] topRightSubTiles;

   @Getter
   private Vector2ic[][] bottomLeftSubTiles;

   @Getter
   private Vector2ic[][] bottomRightSubTiles;

   @Getter
   private final Texture texture;
   private final Mesh mesh;

   @Getter
   private final Vector2fc tileSize;

   @Getter
   private final AutoTileLayout layout;
   private final int rows;
   private final int columns;

   @Getter
   private final int setsCount;

   public AutoTileSet(@NonNull Texture texture, @NonNull Mesh mesh, int rows, int columns, @NonNull AutoTileLayout layout) {
      this.texture = texture;
      this.mesh = mesh;
      this.rows = rows;
      this.columns = columns;
      this.tileSize = texture.getSpriteSize();
      this.layout = layout;
      this.setsCount = rows * columns;
      this.textureSubTilesRows = layout.getRows() * 2;
      this.textureSubTilesColumns = layout.getColumns() * 2;

      switch (layout) {
         case LAYOUT_2X2 -> init2x2();
         case LAYOUT_2X3 -> init2x3();
      }
   }

   private void init2x3() {
      var islandSubTiles = new LinkedList<Vector2ic[]>();
      var topLeftSubTiles = new LinkedList<Vector2ic[]>();
      var topRightSubTiles = new LinkedList<Vector2ic[]>();
      var bottomLeftSubTiles = new LinkedList<Vector2ic[]>();
      var bottomRightSubTiles = new LinkedList<Vector2ic[]>();

      for (int setId = 0; setId < setsCount; ++setId) {
         var crossSubTiles = cutSubTiles(setId, CROSS_TILE_2x3);
         var topLeftTileSubTiles = cutSubTiles(setId, TOP_LEFT_TILE_2x3);
         var topRightTileSubTiles = cutSubTiles(setId, TOP_RIGHT_TILE_2x3);
         var bottomLeftTileSubTiles = cutSubTiles(setId, BOTTOM_LEFT_TILE_2x3);
         var bottomRightTileSubTiles = cutSubTiles(setId, BOTTOM_RIGHT_TILE_2x3);

         /*
          * Indexes:
          *  0 - No connected tiles
          *  1 - Left tile is connected
          *  2 - Right tile is connected
          *  3 - Left and Right tiles are connected
          *  4 - Left, Right, and Center tiles are connected.
          */
         var tl3 = crossSubTiles[0];
         var tr3 = crossSubTiles[1];
         var bl3 = crossSubTiles[2];
         var br3 = crossSubTiles[3];

         var tl0 = topLeftTileSubTiles[0];
         var tr2 = topLeftTileSubTiles[1];
         var bl1 = topLeftTileSubTiles[2];
         var br4 = topLeftTileSubTiles[3];

         var tl1 = topRightTileSubTiles[0];
         var tr0 = topRightTileSubTiles[1];
         var bl4 = topRightTileSubTiles[2];
         var br2 = topRightTileSubTiles[3];

         var tl2 = bottomLeftTileSubTiles[0];
         var tr4 = bottomLeftTileSubTiles[1];
         var bl0 = bottomLeftTileSubTiles[2];
         var br1 = bottomLeftTileSubTiles[3];

         var tl4 = bottomRightTileSubTiles[0];
         var tr1 = bottomRightTileSubTiles[1];
         var bl2 = bottomRightTileSubTiles[2];
         var br0 = bottomRightTileSubTiles[3];

         islandSubTiles.add(cutSubTiles(setId, ISLAND_TILE_2x3));
         topLeftSubTiles.add(new Vector2ic[]{tl0, tl1, tl2, tl3, tl4});
         topRightSubTiles.add(new Vector2ic[]{tr0, tr1, tr2, tr3, tr4});
         bottomLeftSubTiles.add(new Vector2ic[]{bl0, bl1, bl2, bl3, bl4});
         bottomRightSubTiles.add(new Vector2ic[]{br0, br1, br2, br3, br4});
      }

      this.islandSubTiles = islandSubTiles.toArray(new Vector2ic[islandSubTiles.size()][]);
      this.topLeftSubTiles = topLeftSubTiles.toArray(new Vector2ic[topLeftSubTiles.size()][]);
      this.topRightSubTiles = topRightSubTiles.toArray(new Vector2ic[topRightSubTiles.size()][]);
      this.bottomLeftSubTiles = bottomLeftSubTiles.toArray(new Vector2ic[bottomLeftSubTiles.size()][]);
      this.bottomRightSubTiles = bottomRightSubTiles.toArray(new Vector2ic[bottomRightSubTiles.size()][]);
   }

   private void init2x2() {
      var topLeftSubTiles = new LinkedList<Vector2ic[]>();
      var topRightSubTiles = new LinkedList<Vector2ic[]>();
      var bottomLeftSubTiles = new LinkedList<Vector2ic[]>();
      var bottomRightSubTiles = new LinkedList<Vector2ic[]>();

      for (int setId = 0; setId < setsCount; ++setId) {
         var topLeftTileSubTiles = cutSubTiles(setId, TOP_LEFT_TILE_2x2);
         var topRightTileSubTiles = cutSubTiles(setId, TOP_RIGHT_TILE_2x2);
         var bottomLeftTileSubTiles = cutSubTiles(setId, BOTTOM_LEFT_TILE_2x2);
         var bottomRightTileSubTiles = cutSubTiles(setId, BOTTOM_RIGHT_TILE_2x2);

         /*
          * Indexes:
          *  0 - No connected tiles
          *  1 - Left tile is connected
          *  2 - Right tile is connected
          *  3 - Left, Right, and Center tiles are connected.
          */

         var tl0 = topLeftTileSubTiles[0];
         var tr2 = topLeftTileSubTiles[1];
         var bl1 = topLeftTileSubTiles[2];
         var br3 = topLeftTileSubTiles[3];

         var tl1 = topRightTileSubTiles[0];
         var tr0 = topRightTileSubTiles[1];
         var bl3 = topRightTileSubTiles[2];
         var br2 = topRightTileSubTiles[3];

         var tl2 = bottomLeftTileSubTiles[0];
         var tr3 = bottomLeftTileSubTiles[1];
         var bl0 = bottomLeftTileSubTiles[2];
         var br1 = bottomLeftTileSubTiles[3];

         var tl3 = bottomRightTileSubTiles[0];
         var tr1 = bottomRightTileSubTiles[1];
         var bl2 = bottomRightTileSubTiles[2];
         var br0 = bottomRightTileSubTiles[3];

         topLeftSubTiles.add(new Vector2ic[]{tl0, tl1, tl2, tl3});
         topRightSubTiles.add(new Vector2ic[]{tr0, tr1, tr2, tr3});
         bottomLeftSubTiles.add(new Vector2ic[]{bl0, bl1, bl2, bl3});
         bottomRightSubTiles.add(new Vector2ic[]{br0, br1, br2, br3});
      }

      this.topLeftSubTiles = topLeftSubTiles.toArray(new Vector2ic[topLeftSubTiles.size()][]);
      this.topRightSubTiles = topRightSubTiles.toArray(new Vector2ic[topRightSubTiles.size()][]);
      this.bottomLeftSubTiles = bottomLeftSubTiles.toArray(new Vector2ic[bottomLeftSubTiles.size()][]);
      this.bottomRightSubTiles = bottomRightSubTiles.toArray(new Vector2ic[bottomRightSubTiles.size()][]);
   }

   private Vector2ic[] cutSubTiles(int setId, Vector2ic tile) {
      var topLeft = getTile(setId, tile.y() * 2, tile.x() * 2);
      var topRight = getTile(setId, tile.y() * 2, tile.x() * 2 + 1);
      var bottomLeft = getTile(setId, tile.y() * 2 + 1, tile.x() * 2);
      var bottomRight = getTile(setId, tile.y() * 2 + 1, tile.x() * 2 + 1);

      return new Vector2ic[]{topLeft, topRight, bottomLeft, bottomRight};
   }

   private Vector2ic getTile(int setId, int row, int column) {
      return new Vector2i(((setId / columns) * textureSubTilesRows) + row, ((setId % columns) * textureSubTilesColumns) + column);
   }

   public AutoTile createTile(int setId) {
      return new AutoTile(mesh, this, setId);
   }
}
