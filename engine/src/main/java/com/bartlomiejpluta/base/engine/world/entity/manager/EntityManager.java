package com.bartlomiejpluta.base.engine.world.entity.manager;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface EntityManager extends Cleanable {
   Entity createEntity(String entitySetUid);
}
