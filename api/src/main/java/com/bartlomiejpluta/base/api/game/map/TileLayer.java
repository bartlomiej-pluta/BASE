package com.bartlomiejpluta.base.api.game.map;

public interface TileLayer extends Layer {
   void setTile(int row, int column, int tileId);

   void setTile(int row, int column, int tileSetRow, int tileSetColumn);

   void clearTile(int row, int column);
}
