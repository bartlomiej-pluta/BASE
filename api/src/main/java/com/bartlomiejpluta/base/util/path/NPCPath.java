package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.move.Direction;

public class NPCPath extends Path<NPC> {

   public NPCPath turn(Direction direction) {
      path.add(new TurnSegment<>(direction));
      return this;
   }

   @Override
   public NPCPath add(PathSegment<NPC> segment) {
      super.add(segment);
      return this;
   }

   @Override
   public NPCPath addFirst(PathSegment<NPC> segment) {
      super.addFirst(segment);
      return this;
   }

   @Override
   public NPCPath move(Direction direction) {
      super.move(direction);
      return this;
   }

   @Override
   public NPCPath move(Direction direction, boolean ignore) {
      super.move(direction, ignore);
      return this;
   }

   @Override
   public NPCPath wait(float seconds) {
      super.wait(seconds);
      return this;
   }

   @Override
   public NPCPath run(Runnable runnable) {
      super.run(runnable);
      return this;
   }
}
