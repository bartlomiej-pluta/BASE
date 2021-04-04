package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.util.path.Path;
import com.bartlomiejpluta.base.util.path.PathExecutor;

public class FollowPathAI<T extends NPC> implements AI {
   private final PathExecutor<T> executor;

   public FollowPathAI(T npc, Integer repeat, Path<T> path) {
      this.executor = new PathExecutor<>(npc, repeat, path);
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      executor.execute(layer, dt);
   }
}
