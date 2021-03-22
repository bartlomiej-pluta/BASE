package com.bartlomiejpluta.base.util.path;

import com.bartlomiejpluta.base.api.ai.NPC;
import com.bartlomiejpluta.base.api.move.Direction;

public class NPCPath extends Path<NPC> {

   public NPCPath turn(Direction direction) {
      path.add(new TurnSegment<>(direction));
      return this;
   }
}
