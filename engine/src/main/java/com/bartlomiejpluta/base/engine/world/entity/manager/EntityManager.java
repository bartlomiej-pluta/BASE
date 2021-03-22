package com.bartlomiejpluta.base.engine.world.entity.manager;

import com.bartlomiejpluta.base.api.entity.Entity;
import com.bartlomiejpluta.base.engine.common.init.Initianizable;
import com.bartlomiejpluta.base.internal.gc.Cleanable;

public interface EntityManager extends Initianizable, Cleanable {
   Entity createEntity(String entitySetUid);
}
