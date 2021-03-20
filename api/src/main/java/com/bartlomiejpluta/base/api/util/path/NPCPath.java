package com.bartlomiejpluta.base.api.util.path;

import com.bartlomiejpluta.base.api.game.ai.NPC;
import com.bartlomiejpluta.base.api.game.entity.Direction;

public class NPCPath extends Path<NPC> {

   public NPCPath turn(Direction direction) {
      path.add(new TurnSegment<>(direction));
      return this;
   }
}
