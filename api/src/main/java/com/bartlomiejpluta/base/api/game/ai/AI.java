package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

@FunctionalInterface
public interface AI {
   void nextActivity(ObjectLayer layer, float dt);
}
