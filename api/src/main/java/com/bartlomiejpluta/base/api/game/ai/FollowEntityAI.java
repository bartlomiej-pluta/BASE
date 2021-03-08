package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.pathfinding.AstarPathFinder;
import com.bartlomiejpluta.base.api.util.pathfinding.PathFinder;
import org.joml.Vector2i;

public class FollowEntityAI implements AI {
   private static final float recalculateInterval = 0.5f;

   private final NPC npc;
   private final Entity target;
   private final PathFinder pathFinder = new AstarPathFinder(100);

   private final int range;
   private float accumulator = 0.0f;

   public FollowEntityAI(NPC npc, Entity target, int range) {
      this.npc = npc;
      this.range = range;
      this.target = target;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      var distance = npc.manhattanDistance(target);

      if (!npc.isMoving() && distance > 1 && distance < range && accumulator >= recalculateInterval) {
         var path = pathFinder.findPath(layer, npc.getCoordinates(), target.getCoordinates());

         if (!path.isEmpty()) {
            accumulator = recalculateInterval;

            var node = new Vector2i(path.getLast()).sub(npc.getCoordinates());
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

