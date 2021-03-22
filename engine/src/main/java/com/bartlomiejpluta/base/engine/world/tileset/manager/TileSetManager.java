package com.bartlomiejpluta.base.engine.world.tileset.manager;

import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.world.tileset.asset.TileSetAsset;
import com.bartlomiejpluta.base.engine.world.tileset.model.TileSet;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface TileSetManager extends AssetManager<TileSetAsset, TileSet>, Cleanable {
}
