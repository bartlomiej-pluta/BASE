package com.bartlomiejpluta.base.game.map.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.common.manager.AssetManager;
import com.bartlomiejpluta.base.game.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.game.map.model.DefaultGameMap;

public interface MapManager extends AssetManager<GameMapAsset, DefaultGameMap>, Cleanable {
}
