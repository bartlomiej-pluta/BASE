package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.util.path.NPCPath;
import com.bartlomiejpluta.base.api.util.path.PathExecutor;

public class FollowPathAI implements AI {
   private final PathExecutor<NPC> executor;
   private final NPCPath path;

   public FollowPathAI(NPC npc) {
      this(npc, true);
   }

   public FollowPathAI(NPC npc, boolean repeat) {
      var path = new NPCPath();
      this.executor = new PathExecutor<>(npc, repeat, path);
      this.path = path;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      executor.execute(layer, dt);
   }


   public FollowPathAI move(Direction direction) {
      path.move(direction);
      return this;
   }

   public FollowPathAI move(Direction direction, boolean ignore) {
      path.move(direction, ignore);
      return this;
   }

   public FollowPathAI turn(Direction direction) {
      path.turn(direction);
      return this;
   }

   public FollowPathAI wait(float seconds) {
      path.wait(seconds);
      return this;
   }

   public FollowPathAI run(Runnable runnable) {
      path.run(runnable);
      return this;
   }
}
