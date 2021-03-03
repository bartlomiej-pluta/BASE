package com.bartlomiejpluta.base.engine.world.entity.manager;

import com.bartlomiejpluta.base.engine.gc.Cleanable;
import com.bartlomiejpluta.base.engine.world.entity.model.DefaultEntity;

public interface EntityManager extends Cleanable {
   DefaultEntity createEntity(String entitySetUid);
}
