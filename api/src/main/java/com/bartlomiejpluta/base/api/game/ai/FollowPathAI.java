package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.entity.Movement;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

import java.util.LinkedList;
import java.util.List;

public class FollowPathAI implements AI {
   private final List<PathSegment> path = new LinkedList<>();
   private final NPC npc;
   private final boolean repeat;

   private int current = 0;

   public FollowPathAI(NPC npc) {
      this(npc, true);
   }

   public FollowPathAI(NPC npc, boolean repeat) {
      this.npc = npc;
      this.repeat = repeat;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      if (!repeat && isRetired()) {
         return;
      }

      if (!npc.isMoving()) {
         PathSegment item = (PathSegment) path.get(current % path.size());
         if (item.perform(npc, layer, dt)) {
            ++current;
         }
      }
   }

   public boolean isRetired() {
      return current == path.size() - 1;
   }

   public FollowPathAI move(Direction direction) {
      return move(direction, false);
   }

   public FollowPathAI move(Direction direction, boolean ignore) {
      path.add(new Move(direction, ignore));
      return this;
   }

   public FollowPathAI turn(Direction direction) {
      path.add(new Turn(direction));
      return this;
   }

   public FollowPathAI wait(float seconds) {
      path.add(new Wait(seconds));
      return this;
   }

   public FollowPathAI run(Runnable runnable) {
      path.add(new Run(runnable));
      return this;
   }

   public interface PathSegment {
      boolean perform(NPC npc, ObjectLayer layer, float dt);
   }

   private static class Move implements PathSegment {
      private final Direction direction;
      private final boolean ignore;

      public Move(Direction direction, boolean ignore) {
         this.direction = direction;
         this.ignore = ignore;
      }

      @Override
      public boolean perform(NPC npc, ObjectLayer layer, float dt) {
         Movement movement = npc.prepareMovement(direction);

         if (ignore || layer.isTileReachable(movement.getTo())) {
            layer.pushMovement(movement);
            return true;
         }

         return false;
      }
   }

   private static class Turn implements PathSegment {
      private final Direction direction;

      public Turn(Direction direction) {
         this.direction = direction;
      }

      @Override
      public boolean perform(NPC npc, ObjectLayer layer, float dt) {
         npc.setFaceDirection(direction);
         return true;
      }
   }

   private static class Wait implements PathSegment {
      private float accumulator = 0.0f;
      private final float seconds;

      public Wait(float seconds) {
         this.seconds = seconds;
      }

      @Override
      public boolean perform(NPC npc, ObjectLayer layer, float dt) {
         accumulator += dt;

         if (accumulator > seconds) {
            accumulator = 0.0f;
            return true;
         }

         return false;
      }
   }

   private static class Run implements PathSegment {
      private final Runnable runnable;

      private Run(Runnable runnable) {
         this.runnable = runnable;
      }

      @Override
      public boolean perform(NPC npc, ObjectLayer layer, float dt) {
         runnable.run();
         return true;
      }
   }
}
