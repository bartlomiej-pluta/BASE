package com.bartlomiejpluta.base.core.world.tileset.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.core.world.tileset.model.TileSet;

public interface TileSetManager extends Cleanable {
   TileSet createTileSet(String tileSetFileName, int rows, int columns);
}
