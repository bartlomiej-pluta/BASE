package com.bartlomiejpluta.base.api.rule;

import com.bartlomiejpluta.base.api.entity.Entity;

public interface Rule {
   boolean when(Entity entity);

   void then(Entity entity);
}
