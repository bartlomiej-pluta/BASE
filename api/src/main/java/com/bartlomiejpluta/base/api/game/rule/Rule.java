package com.bartlomiejpluta.base.api.game.rule;

import com.bartlomiejpluta.base.api.game.entity.Entity;

public interface Rule {
   boolean when(Entity entity);

   void then(Entity entity);
}
