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
import org.joml.Vector2i;
import org.joml.Vector2ic;

import java.util.ArrayList;

public abstract class KeepStraightDistanceAI<N extends NPC, T extends Locationable> implements AI {
   private final N npc;
   @Setter(onParam = @__(@NonNull))
   private T target;

   private final PathFinder finder;
   private final PathExecutor<N> executor;
   private final int minRange;
   private final int maxRange;

   private MovementPath<N> path = null;

   public KeepStraightDistanceAI(@NonNull PathFinder finder, @NonNull N npc, @NonNull T target, int minRange, int maxRange) {
      this.npc = npc;
      this.target = target;
      this.minRange = minRange;
      this.maxRange = maxRange;
      this.finder = finder;
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
      if (npc.isMoving()) {
         return;
      }

      if (!sees(npc, target, layer)) {
         idle(npc, target, layer, dt);
      }

      if (path == null || path.isEmpty()) {
         // We are considering only straight positions against the target ("@"), for example
         // when minRange is 3 and maxRange is 6, then we are considering only "O"-marked positions.
         // The X means some obstacle for which we'd like to prune the positions after that:
         //               5   4   3   2   1   0   1   2   3   4   5
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   |
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   | 5
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   | 4
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   | 3
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | 2
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | 1
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   | O | O | O | O |   |   | @ |   |   | O | X |   |   |   |   |   | 0
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | 1
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   | 2
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   | 3
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   | 4
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   | 5
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   | O |   |   |   |   |   |   |   |   |   |
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         // |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |
         // +---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
         var consideredPositions = new ArrayList<Vector2ic>(4 * (maxRange - minRange + 1));
         var x = target.getCoordinates().x();
         var y = target.getCoordinates().y();
         var right = true;
         var up = true;
         var left = true;
         var down = true;
         for (int i = 1; i <= maxRange; ++i) {
            var vecUp = new Vector2i(x, y + i);
            var vecRight = new Vector2i(x + i, y);
            var vecDown = new Vector2i(x, y - i);
            var vecLeft = new Vector2i(x - i, y);

            // We are pruning the directions that are
            // blocked by some obstacle.
            // Apart from using layer.isTileReachable() method,
            // we need also to make sure that if the method does return
            // 'true' because the NPC itself is currently occupying given place,
            // which is of course the desired situation.
            // Without this check, we would reject this position from considered list
            // and keep looking for another one which would end up with infinite loop.
            if (up && !layer.isTileReachable(vecUp) && !vecUp.equals(npc.getCoordinates())) up = false;
            if (right && !layer.isTileReachable(vecRight) && !vecRight.equals(npc.getCoordinates())) right = false;
            if (down && !layer.isTileReachable(vecDown) && !vecDown.equals(npc.getCoordinates())) down = false;
            if (left && !layer.isTileReachable(vecLeft) && !vecLeft.equals(npc.getCoordinates())) left = false;

            if (i >= minRange && up) consideredPositions.add(vecUp);
            if (i >= minRange && right) consideredPositions.add(vecRight);
            if (i >= minRange && down) consideredPositions.add(vecDown);
            if (i >= minRange && left) consideredPositions.add(vecLeft);
         }

         consideredPositions.sort(this::comparator);

         // If we are already on any of considered position
         // we abandon finding another path and start to interact
         for (var position : consideredPositions) {
            if (npc.getCoordinates().equals(position)) {
               npc.setFaceDirection(npc.getDirectionTowards(target));
               interact(npc, target, layer, dt);
               return;
            }
         }

         // Otherwise we try to find the best path
         // basing on heuristically considered targets
         for (var position : consideredPositions) {
            if (layer.isTileReachable(position)) {
               path = finder.findPath(layer, npc, position);
               executor.setPath(path);
               break;
            }
         }
      }

      // If no idle and no interact, it means we are following calculated path
      follow(npc, target, layer, dt);
      executor.execute(layer, dt);
   }

   protected abstract boolean sees(N npc, T target, ObjectLayer layer);

   protected abstract void interact(N npc, T target, ObjectLayer layer, float dt);

   protected abstract void follow(N npc, T target, ObjectLayer layer, float dt);

   protected abstract void idle(N npc, T target, ObjectLayer layer, float dt);

   private int comparator(Vector2ic a, Vector2ic b) {
      return Integer.compare(npc.manhattanDistance(a), npc.manhattanDistance(b));
   }
}
