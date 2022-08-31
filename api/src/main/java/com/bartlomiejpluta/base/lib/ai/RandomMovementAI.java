package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;

import java.util.Random;

public class RandomMovementAI<N extends NPC> implements AI {
   private final Random random = new Random();
   private final N npc;

   private final float intervalSeconds;
   private float accumulator = 0.0f;
   private float threshold = 0.0f;

   public RandomMovementAI(N npc, float intervalSeconds) {
      this.npc = npc;
      this.intervalSeconds = intervalSeconds;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (!npc.isMoving()) {
         if (accumulator > threshold) {
            Direction direction = Direction.values()[random.nextInt(4)];
            npc.move(direction);
            accumulator = 0.0f;
            threshold = random.nextFloat() * intervalSeconds;
         }

         accumulator += dt;
      }
   }
}
