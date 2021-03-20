package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.ai.NPC;
import com.bartlomiejpluta.base.api.game.entity.Direction;
import com.bartlomiejpluta.base.api.game.map.layer.object.ObjectLayer;

import static java.util.Objects.requireNonNull;

public class TurnSegment<T extends NPC> implements PathSegment<T> {
   private final Direction direction;

   public TurnSegment(Direction direction) {
      this.direction = requireNonNull(direction);
   }

   @Override
   public boolean perform(T npc, ObjectLayer layer, float dt) {
      npc.setFaceDirection(direction);
      return true;
   }
}
