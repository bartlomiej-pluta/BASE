package com.bartlomiejpluta.base.engine.world.entity.manager;

import com.bartlomiejpluta.base.api.game.entity.Entity;
import com.bartlomiejpluta.base.api.internal.gc.Cleanable;

public interface EntityManager extends Cleanable {
   Entity createEntity(String entitySetUid);
}
