package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.util.math.Distance;
import org.joml.Vector2ic;

import java.util.Random;

public class RandomMovementAI<N extends NPC> implements AI {
   private final Random random = new Random();
   private final N npc;

   private final float intervalSeconds;
   private final int radius;
   private final Vector2ic origin;
   private float accumulator = 0.0f;
   private float threshold = 0.0f;

   public RandomMovementAI(N npc, float intervalSeconds) {
      this.npc = npc;
      this.intervalSeconds = intervalSeconds;
      this.radius = 0;
      origin = null;
   }

   public RandomMovementAI(N npc, float intervalSeconds, Vector2ic origin, int radius) {
      this.npc = npc;
      this.intervalSeconds = intervalSeconds;
      this.radius = radius;
      this.origin = origin;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (!npc.isMoving()) {
         if (accumulator > threshold) {
            var direction = Direction.values()[random.nextInt(4)];
            var movement = npc.prepareMovement(direction);

            if (origin != null && Distance.euclidean(origin, movement.getTo()) > radius) {
               return;
            }

            npc.getLayer().pushMovement(movement);
            accumulator = 0.0f;
            threshold = random.nextFloat() * intervalSeconds;
         }

         accumulator += dt;
      }
   }
}
