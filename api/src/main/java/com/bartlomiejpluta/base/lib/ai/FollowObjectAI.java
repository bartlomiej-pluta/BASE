package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.location.Locationable;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.MoveEvent;
import com.bartlomiejpluta.base.util.path.MovementPath;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import com.bartlomiejpluta.base.util.pathfinder.PathFinder;
import lombok.NonNull;
import lombok.Setter;

public abstract class FollowObjectAI<N extends NPC, T extends Locationable> implements AI {

   private final PathFinder finder;
   private final PathExecutor<N> executor;
   private final N npc;

   @Setter(onParam = @__(@NonNull))
   private T target;

   private MovementPath<N> path = null;

   protected FollowObjectAI(@NonNull PathFinder finder, @NonNull N npc, @NonNull T target) {
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
      // or another object is blocking current path
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
            interact(npc, target, layer, dt);
         } else if (sees(npc, target, layer, distance)) {
            follow(npc, target, layer, dt);

            if (path == null) {
               path = finder.findPath(layer, npc, target.getCoordinates());
               executor.setPath(path);
            }

            executor.execute(layer, dt);
         } else {
            idle(npc, target, layer, dt);
         }
      }
   }

   protected abstract boolean sees(N npc, T target, ObjectLayer layer, int distance);

   protected abstract void interact(N npc, T target, ObjectLayer layer, float dt);

   protected abstract void follow(N npc, T target, ObjectLayer layer, float dt);

   protected abstract void idle(N npc, T target, ObjectLayer layer, float dt);
}
