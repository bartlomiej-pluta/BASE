package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.util.path.MovementPath;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import com.bartlomiejpluta.base.util.pathfinder.PathFinder;
import lombok.NonNull;
import lombok.Setter;

public abstract class FollowEntityAI<N extends NPC, T extends Entity> implements AI {

   private final PathFinder finder;
   private final PathExecutor<N> executor;
   private final N npc;
   private final T target;

   @Setter
   private AI idleAI;

   @Setter
   private int range = 20;

   private MovementPath<N> path = null;

   protected FollowEntityAI(@NonNull PathFinder finder, @NonNull N npc, @NonNull T target) {
      this.finder = finder;
      this.npc = npc;
      this.target = target;
      this.executor = new PathExecutor<>(npc);
   }

   public void recomputePath() {
      path = null;
   }

   public void recomputePath(@NonNull MoveEvent event) {
      var movable = event.getMovable();

      // Refresh only when target has been displaced
      // or another entity is blocking current path
      if (movable == target || (path != null && path.contains(movable))) {
         path = null;
      }
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (!npc.isMoving()) {
         var distance = npc.manhattanDistance(target);

         if (distance == 1) {
            npc.setFaceDirection(npc.getDirectionTowards(target));
            interact(npc, target);
         } else if (distance < range) {
            follow(npc, target);

            if (path == null) {
               path = finder.findPath(layer, npc, target.getCoordinates());
               executor.setPath(path);
            }

            executor.execute(layer, dt);
         } else {
            idle(npc, target);

            if (idleAI != null) {
               idleAI.nextActivity(layer, dt);
            }
         }
      }
   }

   protected abstract void interact(N npc, T target);

   protected abstract void follow(N npc, T target);

   protected abstract void idle(N npc, T target);
}
