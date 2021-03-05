package com.bartlomiejpluta.base.engine.world.map.manager;

import com.bartlomiejpluta.base.api.game.context.Context;
import com.bartlomiejpluta.base.api.game.map.handler.MapHandler;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;
import com.bartlomiejpluta.base.engine.common.manager.AssetManager;
import com.bartlomiejpluta.base.engine.world.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.engine.world.map.model.DefaultGameMap;

public interface MapManager extends AssetManager<GameMapAsset, DefaultGameMap>, Cleanable {
   MapHandler loadHandler(Context context, String mapUid);
}
