package com.bartlomiejpluta.base.api.map.layer.tile;

import com.bartlomiejpluta.base.api.map.layer.base.Layer;

public interface TileLayer extends Layer {
   void setTile(int row, int column, int tileId);

   void setTile(int row, int column, int tileSetRow, int tileSetColumn);

   void clearTile(int row, int column);
}
