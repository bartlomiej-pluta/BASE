package com.bartlomiejpluta.base.api.game.rule;

import com.bartlomiejpluta.base.api.game.entity.Entity;

public interface Rule {
   boolean test(Entity entity);

   void invoke(Entity entity);
}
