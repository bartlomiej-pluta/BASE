package com.bartlomiejpluta.base.api.snippet.ai;

import com.bartlomiejpluta.base.api.game.ai.AI;
import com.bartlomiejpluta.base.api.game.ai.NPC;
import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

import java.util.Random;

public class RandomMovementAI implements AI {
   private final Random random = new Random();
   private final NPC npc;

   private final float intervalSeconds;
   private float accumulator = 0.0f;
   private float threshold = 0.0f;

   public RandomMovementAI(NPC npc, float intervalSeconds) {
      this.npc = npc;
      this.intervalSeconds = intervalSeconds;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (!npc.isMoving()) {
         if (accumulator > threshold) {
            Direction direction = Direction.values()[random.nextInt(4)];
            layer.pushMovement(npc.prepareMovement(direction));
            accumulator = 0.0f;
            threshold = random.nextFloat() * intervalSeconds;
         }

         accumulator += dt;
      }
   }
}
