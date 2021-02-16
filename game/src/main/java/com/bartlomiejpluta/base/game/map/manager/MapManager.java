package com.bartlomiejpluta.base.game.map.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.map.asset.GameMapAsset;
import com.bartlomiejpluta.base.game.map.model.GameMap;

public interface MapManager extends Cleanable {
   void registerAsset(GameMapAsset asset);

   GameMap loadMap(String uid);
}
