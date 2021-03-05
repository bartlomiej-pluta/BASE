package com.bartlomiejpluta.base.api.game.ai;

import com.bartlomiejpluta.base.api.game.entity.Entity;

public interface NPC extends Entity {
   AI getStrategy();
}
