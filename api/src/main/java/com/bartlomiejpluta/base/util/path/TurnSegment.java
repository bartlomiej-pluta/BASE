package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.map.layer.object.ObjectLayer;
import com.bartlomiejpluta.base.api.move.Direction;

import static java.util.Objects.requireNonNull;

public class TurnSegment<T extends NPC> implements PathSegment<T> {
   private final Direction direction;

   public TurnSegment(Direction direction) {
      this.direction = requireNonNull(direction);
   }

   @Override
   public PathProgress perform(T npc, ObjectLayer layer, float dt) {
      npc.setFaceDirection(direction);
      return PathProgress.SEGMENT_DONE;
   }
}
