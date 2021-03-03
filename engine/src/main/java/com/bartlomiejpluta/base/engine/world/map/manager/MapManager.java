package com.bartlomiejpluta.base.engine.world.map.manager;

import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.gc.Cleanable;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;

public interface MapManager extends AssetManager<GameMapAsset, DefaultGameMap>, Cleanable {
}
