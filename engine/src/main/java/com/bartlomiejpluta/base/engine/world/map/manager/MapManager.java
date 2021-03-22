package com.bartlomiejpluta.base.engine.world.map.manager;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.map.handler.MapHandler;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface MapManager extends AssetManager<GameMapAsset, DefaultGameMap>, Cleanable {
   MapHandler loadHandler(Context context, String mapUid);
}
