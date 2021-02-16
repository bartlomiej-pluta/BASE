package com.bartlomiejpluta.base.game.world.tileset.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.world.tileset.model.TileSet;

public interface TileSetManager extends Cleanable {
   TileSet createTileSet(String tileSetFileName, int rows, int columns);
}
