package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;

public class NoopAI implements AI {

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      // do nothing
   }

   public static AI INSTANCE = new NoopAI();
}
