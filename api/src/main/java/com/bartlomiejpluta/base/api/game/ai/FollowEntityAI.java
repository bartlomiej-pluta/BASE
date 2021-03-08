package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.pathfinder.PathFinder;
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
