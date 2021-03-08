package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.pathfinding.AstarPathFinder;
import com.bartlomiejpluta.base.api.util.pathfinding.PathFinder;
import org.joml.Vector2i;

public class FollowEntityAI implements AI {
   private final NPC npc;
   private final Entity target;
   private final PathFinder pathFinder = new AstarPathFinder();

   public FollowEntityAI(NPC npc, Entity target) {
      this.npc = npc;
      this.target = target;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (!npc.isMoving() && npc.manhattanDistance(target) > 1) {
         var path = pathFinder.findPath(layer, npc.getCoordinates(), target.getCoordinates(), 30);

         if (!path.isEmpty()) {
            var node = new Vector2i(path.getLast()).sub(npc.getCoordinates());
            var direction = Direction.ofVector(node);
            var movement = npc.prepareMovement(direction);
            layer.pushMovement(movement);
         }
      }
   }
}

