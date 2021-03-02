package com.bartlomiejpluta.base.api.context;

import com.bartlomiejpluta.base.api.entity.Entity;

public interface Context {
   void openMap(String mapUid);

   Entity createEntity(String entitySetUid);
}
