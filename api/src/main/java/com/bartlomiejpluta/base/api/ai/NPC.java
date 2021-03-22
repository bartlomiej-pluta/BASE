package com.bartlomiejpluta.base.api.ai;

import com.bartlomiejpluta.base.api.entity.Entity;

public interface NPC extends Entity {
   AI getStrategy();
}
