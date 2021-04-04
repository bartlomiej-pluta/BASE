package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;
import com.bartlomiejpluta.base.util.pathfinder.PathFinder;
import org.joml.Vector2i;

public class FollowEntityAI implements AI {
   private final NPC npc;
   private final Entity target;
   private final PathFinder pathFinder;
   private final int range;

   private final float recalculateInterval;
   private float accumulator = 0.0f;

   public FollowEntityAI(PathFinder pathFinder, float recalculateInterval, NPC npc, Entity target, int range) {
      this.pathFinder = pathFinder;
      this.recalculateInterval = recalculateInterval;
      this.npc = npc;
      this.range = range;
      this.target = target;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      var distance = npc.manhattanDistance(target);

      if (!npc.isMoving() && 1 < distance && distance < range && accumulator >= recalculateInterval) {
         var path = pathFinder.findSequence(layer, npc.getCoordinates(), target.getCoordinates());

         if (!path.isEmpty()) {
            accumulator = recalculateInterval;

            var node = path.getFirst().sub(npc.getCoordinates(), new Vector2i());
            var direction = Direction.ofVector(node);
            var movement = npc.prepareMovement(direction);
            layer.pushMovement(movement);
         } else {
            accumulator = 0.0f;
         }

      }

      accumulator += dt;
   }
}
