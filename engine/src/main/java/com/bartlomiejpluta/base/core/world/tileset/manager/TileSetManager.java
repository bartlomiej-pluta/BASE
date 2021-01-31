package com.bartlomiejpluta.base.core.world.tileset.manager;

import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;

public interface TileSetManager {
   TileSet createTileSet(String tileSetFileName, int rows, int columns);
}
