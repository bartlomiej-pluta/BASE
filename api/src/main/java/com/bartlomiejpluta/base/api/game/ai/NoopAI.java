package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

public class NoopAI implements AI {

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      // do nothing
   }

   public static AI INSTANCE = new NoopAI();
}
