package com.bartlomiejpluta.base.game.world.tileset.manager;

import com.bartlomiejpluta.base.game.world.tileset.model.TileSet;

public interface TileSetManager {
   TileSet createTileSet(String tileSetFileName, int rows, int columns);
}
