package com.bartlomiejpluta.base.game.entity.manager;

import com.bartlomiejpluta.base.core.gc.Cleanable;
import com.bartlomiejpluta.base.game.entity.model.DefaultEntity;

public interface EntityManager extends Cleanable {
   DefaultEntity createEntity(String entitySetUid);
}
