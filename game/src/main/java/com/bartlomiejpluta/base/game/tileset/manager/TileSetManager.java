package com.bartlomiejpluta.base.game.tileset.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.common.manager.AssetManager;
import com.bartlomiejpluta.base.game.tileset.asset.TileSetAsset;
import com.bartlomiejpluta.base.game.tileset.model.TileSet;

public interface TileSetManager extends AssetManager<TileSetAsset, TileSet>, Cleanable {
}
