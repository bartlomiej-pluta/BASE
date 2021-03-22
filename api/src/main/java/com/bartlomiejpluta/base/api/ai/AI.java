package com.bartlomiejpluta.base.api.ai;

import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;

@FunctionalInterface
public interface AI {
   void nextActivity(ObjectLayer layer, float dt);
}
