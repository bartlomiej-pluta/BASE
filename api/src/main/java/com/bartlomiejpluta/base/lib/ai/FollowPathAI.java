package com.bartlomiejpluta.base.lib.ai;

import com.bartlomiejpluta.base.api.ai.AI;
import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.util.path.Path;
import com.bartlomiejpluta.base.util.path.PathExecutor;
import com.bartlomiejpluta.base.util.path.PathProgress;
import lombok.Getter;

public class FollowPathAI<T extends NPC> implements AI {
   private final PathExecutor<T> executor;

   @Getter
   private PathProgress progress = PathProgress.NOT_STARTED;

   public FollowPathAI(T npc) {
      this.executor = new PathExecutor<>(npc);
   }

   public FollowPathAI<T> setPath(Path<T> path) {
      executor.setPath(path);
      return this;
   }

   public FollowPathAI<T> setRepeat(Integer repeat) {
      executor.setRepeat(repeat);
      return this;
   }

   public FollowPathAI<T> reset() {
      executor.reset();
      return this;
   }

   @Override
   public void nextActivity(ObjectLayer layer, float dt) {
      progress = executor.execute(layer, dt);
   }
}
